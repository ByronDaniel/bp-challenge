package com.challenge.client.application.input.port;

import com.challenge.client.domain.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientInputPort {

  Flux<Client> getAll();

  Mono<Client> getById(Integer id);

  Mono<Client> save(Client client);

  Mono<Client> updateById(Integer id, Client client);

  Mono<Void> deleteById(Integer id);
}
