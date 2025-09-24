package com.challenge.movement.infrastructure.output.adapter;

import com.challenge.movement.application.output.port.MovementOutputPort;
import com.challenge.movement.domain.Movement;
import com.challenge.movement.infrastructure.output.repository.MovementRepository;
import com.challenge.movement.infrastructure.output.repository.mapper.MovementEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MovementAdapter implements MovementOutputPort {

  MovementEntityMapper movementEntityMapper;
  MovementRepository movementRepository;

  @Override
  public Flux<Movement> findAll() {
    return movementRepository.findAll()
        .map(movementEntityMapper::toMovement);
  }

  @Override
  public Flux<Movement> findByAccountId(Integer id) {
    return movementRepository.findByAccountId(id)
        .map(movementEntityMapper::toMovement);
  }

  @Override
  public Mono<Movement> save(Movement movement) {
    return movementRepository.save(movementEntityMapper.toMovementEntity(movement))
        .map(movementEntityMapper::toMovement);
  }
}
