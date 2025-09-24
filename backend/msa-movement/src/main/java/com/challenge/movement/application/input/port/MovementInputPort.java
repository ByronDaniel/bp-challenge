package com.challenge.movement.application.input.port;

import com.challenge.movement.domain.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementInputPort {

  Flux<Movement> getAllByOptionalFilter(String numberAccount);

  Mono<Movement> save(Movement movement);
}
