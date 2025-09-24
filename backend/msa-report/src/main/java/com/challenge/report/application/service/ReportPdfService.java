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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReportPdfService implements ReportPdfInputPort {

  ClientOutputPort clientOutputPort;
  AccountOutputPort accountOutputPort;
  MovementOutputPort movementOutputPort;
  ReportMapper reportMapper;
  PdfGeneratorOutputPort pdfGeneratorOutputPort;

  public Mono<ReportPdf> getReportWithPdf(String date, Integer clientId) {
    return getReportByFilter(date, clientId)
        .collectList()
        .flatMap(reports -> pdfGeneratorOutputPort.generatePdfFromReports(reports)
            .flatMap(pdfString -> {
              ReportPdf reportPdf = new ReportPdf(reports, pdfString);
              return Mono.just(reportPdf);
            }));
  }

  private Flux<Report> getReportByFilter(String date, Integer clientId) {
    DateRange dateRange = parseDateRange(date);
    return clientOutputPort.getById(clientId)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMapMany(client ->
            accountOutputPort.getAll(client.getClientId())
                .flatMap(account ->
                    movementOutputPort.getAll(account.getId())
                        .filter(movement -> isWithinRange(movement, dateRange))
                        .map(movement -> reportMapper.toReport(client, account, movement))
                )
        );
  }
}