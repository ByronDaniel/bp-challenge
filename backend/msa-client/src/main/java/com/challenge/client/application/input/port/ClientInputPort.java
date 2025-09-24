package com.challenge.client.application.input.port;

import com.challenge.client.domain.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientInputPort {

  Flux<Client> getAll(String identification);

  Mono<Client> save(Client client);

  Mono<Client> update(Client client);

  Mono<Void> deleteByIdentification(String identification);
}
