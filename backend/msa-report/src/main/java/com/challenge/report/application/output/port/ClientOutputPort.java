package com.challenge.report.application.output.port;

import com.challenge.report.domain.Client;
import reactor.core.publisher.Flux;

public interface ClientOutputPort {

  Flux<Client> getAll(String identification);
}
