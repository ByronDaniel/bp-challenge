package com.challenge.report.infrastructure.output.adapter.pdf;

import com.challenge.report.application.output.port.PdfGeneratorOutputPort;
import com.challenge.services.server.models.ReportResponseDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;

@Component
public class PdfGeneratorAdapter implements PdfGeneratorOutputPort {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$#,##0.00");
    private static final String[] HEADERS = { "Fecha", "Cliente", "Número Cuenta", "Tipo", "Estado", "Movimiento",
            "Saldo Inicial", "Valor", "Saldo" };
    private static final float[] COLUMN_WIDTHS = { 10f, 15f, 12f, 12f, 8f, 12f, 12f, 10f, 12f };

    @Override
    public Mono<String> generatePdfFromReports(List<ReportResponseDto> reports) {
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
                for (ReportResponseDto report : reports) {
                    table.addCell(new Phrase(formatValue(report.getDate()), dataFont));
                    table.addCell(new Phrase(formatValue(report.getName()), dataFont));
                    table.addCell(new Phrase(formatValue(report.getNumber()), dataFont));
                    table.addCell(new Phrase(formatValue(report.getTypeAccount()), dataFont));
                    table.addCell(new Phrase(
                            report.getStatus() != null ? (report.getStatus() ? "Activa" : "Inactiva") : "", dataFont));
                    table.addCell(new Phrase(formatValue(report.getType()), dataFont));
                    table.addCell(new Phrase(formatCurrency(report.getInitialBalance()), dataFont));
                    table.addCell(new Phrase(formatCurrency(report.getValue()), dataFont));
                    table.addCell(new Phrase(formatCurrency(report.getBalance()), dataFont));
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

    private String formatCurrency(Double value) {
        return value != null ? CURRENCY_FORMAT.format(value) : "";
    }
}