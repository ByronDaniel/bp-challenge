package com.challenge.report.application.output.port;

import com.challenge.report.domain.Report;
import java.util.List;
import reactor.core.publisher.Mono;

public interface PdfGeneratorOutputPort {

  Mono<String> generatePdfFromReports(List<Report> reports);
}