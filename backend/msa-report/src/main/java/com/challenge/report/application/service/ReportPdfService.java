package com.challenge.report.application.service;

import static com.challenge.report.application.service.utils.Constants.CLIENT_NOT_FOUND;
import static com.challenge.report.application.service.utils.Utils.isWithinRange;
import static com.challenge.report.application.service.utils.Utils.parseDateRange;

import com.challenge.report.application.input.port.ReportPdfInputPort;
import com.challenge.report.application.output.port.AccountOutputPort;
import com.challenge.report.application.output.port.ClientOutputPort;
import com.challenge.report.application.output.port.MovementOutputPort;
import com.challenge.report.application.output.port.PdfGeneratorOutputPort;
import com.challenge.report.application.service.utils.ReportMapper;
import com.challenge.report.domain.DateRange;
import com.challenge.report.domain.Report;
import com.challenge.report.domain.ReportPdf;
import com.challenge.report.infrastructure.exception.NotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReportPdfService implements ReportPdfInputPort {

  private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
      .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
      .optionalStart()
      .appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true)
      .optionalEnd()
      .toFormatter();
  ClientOutputPort clientOutputPort;
  AccountOutputPort accountOutputPort;
  MovementOutputPort movementOutputPort;
  ReportMapper reportMapper;
  PdfGeneratorOutputPort pdfGeneratorOutputPort;

  @Override
  public Mono<ReportPdf> getReportWithPdf(String date, String clientIdentification) {
    return getReportByFilter(date, clientIdentification)
        .collectList()
        .flatMap(reports ->
            pdfGeneratorOutputPort.generatePdfFromReports(reports)
                .map(pdfString -> new ReportPdf(reports, pdfString))
        );
  }

  private Flux<Report> getReportByFilter(String date, String clientIdentification) {
    DateRange dateRange = parseDateRange(date);

    return clientOutputPort.getAll(clientIdentification)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMap(client ->
            accountOutputPort.getAll(clientIdentification)
                .flatMap(account ->
                    movementOutputPort.getAll(account.getNumber())
                        .filter(movement -> isWithinRange(movement, dateRange))
                        .map(movement -> reportMapper.toReport(client, account, movement))
                )
                .sort(this::compareReportsByDate)
        );
  }

  private int compareReportsByDate(Report r1, Report r2) {
    LocalDateTime f1 = LocalDateTime.parse(r1.getMovement().getDate(), DATE_FORMATTER);
    LocalDateTime f2 = LocalDateTime.parse(r2.getMovement().getDate(), DATE_FORMATTER);
    return f1.compareTo(f2);
  }
}