package com.challenge.movement.infrastructure.input.adapter.rest.impl;

import com.challenge.movement.application.input.port.MovementInputPort;
import com.challenge.movement.infrastructure.input.adapter.rest.mapper.MovementDtoMapper;
import com.challenge.services.server.MovimientosApi;
import com.challenge.services.server.models.MovementRequestDto;
import com.challenge.services.server.models.MovementResponseDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class MovementController implements MovimientosApi {

  MovementInputPort movementInputPort;
  MovementDtoMapper movementDtoMapper;

  @Override
  public Mono<ResponseEntity<Flux<MovementResponseDto>>> getAll(Integer accountId,
      ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(movementInputPort.getAllByOptionalFilter(accountId)
        .map(movementDtoMapper::toMovementResponseDto)));
  }

  @Override
  public Mono<ResponseEntity<MovementResponseDto>> save(MovementRequestDto movementRequestDto,
      ServerWebExchange exchange) {
    return movementInputPort.save(movementDtoMapper.toMovement(movementRequestDto))
        .map(movementDtoMapper::toMovementResponseDto)
        .map(movementResponseDto ->
            ResponseEntity.created(URI.create("/movimiento/" + movementResponseDto.getMovementId()))
                .body(movementResponseDto));
  }

}
