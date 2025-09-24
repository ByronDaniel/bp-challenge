package com.challenge.report.infrastructure.output.adapter;

import com.challenge.report.application.output.port.PdfGeneratorOutputPort;
import com.challenge.report.domain.Account;
import com.challenge.report.domain.Client;
import com.challenge.report.domain.Movement;
import com.challenge.report.domain.Report;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class PdfGeneratorAdapter implements PdfGeneratorOutputPort {

  private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");
  private static final String[] HEADERS = {"Fecha", "Cliente", "Número Cuenta", "Tipo", "Estado",
      "Movimiento",
      "Saldo Inicial", "Valor", "Saldo"};
  private static final float[] COLUMN_WIDTHS = {10f, 15f, 12f, 12f, 8f, 12f, 12f, 10f, 12f};

  @Override
  public Mono<String> generatePdfFromReports(List<Report> reports) {
    return Mono.fromCallable(() -> {
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, baos);
        document.open();

        // Título
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Reporte de Estado de Cuenta", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Tabla
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setWidths(COLUMN_WIDTHS);

        // Headers
        Font headerFont = new Font(Font.HELVETICA, 8, Font.BOLD);
        for (String header : HEADERS) {
          PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          cell.setBackgroundColor(new java.awt.Color(230, 230, 230));
          cell.setPadding(4);
          table.addCell(cell);
        }

        // Datos
        Font dataFont = new Font(Font.HELVETICA, 7, Font.NORMAL);
        for (Report report : reports) {
          Movement movement = report.getMovement();
          Client client = report.getClient();
          Account account = report.getAccount();
          table.addCell(new Phrase(formatValue(movement.getDate()), dataFont));
          table.addCell(new Phrase(formatValue(client.getName()), dataFont));
          table.addCell(new Phrase(formatValue(account.getNumber()), dataFont));
          table.addCell(new Phrase(formatValue(account.getType()), dataFont));
          table.addCell(new Phrase(
              account.getStatus() != null ? (account.getStatus() ? "Activa" : "Inactiva") : "",
              dataFont));
          table.addCell(new Phrase(formatValue(movement.getType()), dataFont));
          table.addCell(new Phrase(formatCurrency(account.getBalance()), dataFont));
          table.addCell(new Phrase(formatCurrency(movement.getValue()), dataFont));
          table.addCell(new Phrase(formatCurrency(movement.getBalance()), dataFont));
        }

        document.add(table);
        document.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
      } catch (Exception e) {
        throw new RuntimeException("Error generando PDF", e);
      }
    }).subscribeOn(Schedulers.boundedElastic());
  }

  private String formatValue(Object value) {
    return value != null ? value.toString() : "";
  }

  private String formatCurrency(BigDecimal value) {
    return value != null ? CURRENCY_FORMAT.format(value) : "";
  }
}