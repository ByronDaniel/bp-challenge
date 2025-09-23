package com.challenge.report.infrastructure.input.adapter.rest.impl;

import com.challenge.report.application.usecase.ReportPdfService;
import com.challenge.services.server.ReporteApi;
import com.challenge.services.server.models.ReportPdfResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ReportController implements ReporteApi {

  private final ReportPdfService reportPdfService;

  @Override
  public Mono<ResponseEntity<ReportPdfResponseDto>> getReportByFilter(String date,
      Integer clientId, ServerWebExchange exchange) {
    return reportPdfService.getReportWithPdf(date, clientId)
        .map(ResponseEntity::ok);
  }
}
