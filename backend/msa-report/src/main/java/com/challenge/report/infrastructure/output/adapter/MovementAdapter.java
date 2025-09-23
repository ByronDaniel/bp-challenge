package com.challenge.report.infrastructure.output.adapter;

import com.challenge.report.application.output.port.MovementOutputPort;
import com.challenge.report.domain.Movement;
import com.challenge.report.infrastructure.output.repository.mapper.MovementMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import task___buildSpringClient2__property__packageName_.clients2.client.MovementsApi;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovementAdapter implements MovementOutputPort {

  MovementsApi movementsApi;
  MovementMapper movementMapper;

  @Override
  public Flux<Movement> getAll(Integer accountId) {
    return movementsApi.getAll(accountId)
        .map(movementMapper::toMovement);
  }
}
