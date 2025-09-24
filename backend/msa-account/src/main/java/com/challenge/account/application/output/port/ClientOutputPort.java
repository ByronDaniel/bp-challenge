package com.challenge.account.application.output.port;

import com.challenge.account.domain.Client;
import reactor.core.publisher.Flux;

public interface ClientOutputPort {

  Flux<Client> getAll(String identification);
}
