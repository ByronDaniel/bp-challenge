package com.challenge.report.infrastructure.input.adapter.rest.impl;

import com.challenge.report.application.input.port.ReportInputPort;
import com.challenge.report.infrastructure.input.adapter.rest.mapper.ReportDtoMapper;
import com.challenge.services.server.ReporteApi;
import com.challenge.services.server.models.ReportResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController implements ReporteApi {

  ReportInputPort reportInputPort;
  ReportDtoMapper reportDtoMapper;

  @Override
  public Mono<ResponseEntity<Flux<ReportResponseDto>>> getReportByFilter(String date,
      Integer clientId, ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(reportInputPort.getReportByFilter(date, clientId)
        .map(reportDtoMapper::toReportResponseDto)));
  }
}
