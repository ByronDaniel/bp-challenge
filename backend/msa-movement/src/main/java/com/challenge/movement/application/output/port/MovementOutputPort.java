package com.challenge.movement.application.output.port;

import com.challenge.movement.domain.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementOutputPort {

  Flux<Movement> findAll();

  Flux<Movement> findByAccountId(Integer accountId);

  Mono<Movement> save(Movement movement);
}
