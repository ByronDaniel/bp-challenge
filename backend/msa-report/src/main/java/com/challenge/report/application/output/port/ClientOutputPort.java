package com.challenge.report.application.output.port;

import com.challenge.report.domain.Client;
import reactor.core.publisher.Mono;

public interface ClientOutputPort {

  Mono<Client> getById(Integer id);
}
