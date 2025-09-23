package com.challenge.report.application.input.port;

import com.challenge.services.server.models.ReportPdfResponseDto;
import reactor.core.publisher.Mono;

public interface ReportPdfInputPort {

  Mono<ReportPdfResponseDto> getReportWithPdf(String date, Integer clientId);
}