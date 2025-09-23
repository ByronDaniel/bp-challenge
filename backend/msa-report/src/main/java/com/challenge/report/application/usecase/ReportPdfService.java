package com.challenge.report.application.usecase;

import com.challenge.report.application.input.port.ReportInputPort;
import com.challenge.report.application.output.port.PdfGeneratorOutputPort;
import com.challenge.report.infrastructure.input.adapter.rest.mapper.ReportDtoMapper;
import com.challenge.services.server.models.ReportPdfResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReportPdfService {

  private final ReportInputPort reportInputPort;
  private final PdfGeneratorOutputPort pdfGeneratorOutputPort;
  private final ReportDtoMapper reportDtoMapper;

  public Mono<ReportPdfResponseDto> getReportWithPdf(String date, Integer clientId) {
    return reportInputPort.getReportByFilter(date, clientId)
        .map(reportDtoMapper::toReportResponseDto)
        .collectList()
        .flatMap(reports -> {
          if (reports.isEmpty()) {
            return Mono.just(new ReportPdfResponseDto().pdf("").report(null));
          }
          return pdfGeneratorOutputPort.generatePdfFromReports(reports)
              .map(pdf -> new ReportPdfResponseDto().pdf(pdf).report(reports.get(0)))
              .onErrorReturn(new ReportPdfResponseDto().pdf("").report(reports.get(0)));
        });
  }
}