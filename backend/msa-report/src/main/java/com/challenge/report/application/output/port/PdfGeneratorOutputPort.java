package com.challenge.report.application.output.port;

import com.challenge.services.server.models.ReportResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PdfGeneratorOutputPort {

  Mono<String> generatePdfFromReports(List<ReportResponseDto> reports);
}