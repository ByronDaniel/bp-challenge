package com.challenge.report.application.input.port;

import com.challenge.report.domain.ReportPdf;
import reactor.core.publisher.Mono;

public interface ReportPdfInputPort {

  Mono<ReportPdf> getReportWithPdf(String date, String clientIdentification);
}