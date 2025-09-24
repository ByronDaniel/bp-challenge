package com.challenge.report.infrastructure.input.adapter.rest.impl;

import com.challenge.report.application.input.port.ReportPdfInputPort;
import com.challenge.report.infrastructure.input.adapter.rest.mapper.ReportDtoMapper;
import com.challenge.services.server.ReporteApi;
import com.challenge.services.server.models.ReportPdfResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReportController implements ReporteApi {

  ReportPdfInputPort reportPdfInputPort;
  ReportDtoMapper reportDtoMapper;

  @Override
  public Mono<ResponseEntity<ReportPdfResponseDto>> getReportByFilter(String date,
      String clientIdentification, ServerWebExchange exchange) {
    return reportPdfInputPort.getReportWithPdf(date, clientIdentification)
        .map(reportDtoMapper::toReportResponseDto)
        .map(ResponseEntity::ok);
  }
}
