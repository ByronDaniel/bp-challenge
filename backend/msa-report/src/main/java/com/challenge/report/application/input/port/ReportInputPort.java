package com.challenge.report.application.input.port;

import com.challenge.report.domain.Report;
import reactor.core.publisher.Flux;

public interface ReportInputPort {

  Flux<Report> getReportByFilter(String date, Integer clientId);
}
