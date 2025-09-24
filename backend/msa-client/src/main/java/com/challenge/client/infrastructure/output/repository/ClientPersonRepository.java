package com.challenge.client.infrastructure.output.repository;

import com.challenge.client.infrastructure.output.repository.entity.ClientEntity;
import com.challenge.client.infrastructure.output.repository.entity.PersonEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public interface ClientPersonRepository {

  Flux<Tuple2<ClientEntity, PersonEntity>> findAll();

  Mono<Tuple2<ClientEntity, PersonEntity>> findByIdentification(String identification);
}
