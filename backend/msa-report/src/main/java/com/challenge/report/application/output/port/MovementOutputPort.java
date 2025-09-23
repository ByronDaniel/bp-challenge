package com.challenge.report.application.output.port;

import com.challenge.report.domain.Movement;
import reactor.core.publisher.Flux;

public interface MovementOutputPort {

  Flux<Movement> getAll(Integer accountId);
}
