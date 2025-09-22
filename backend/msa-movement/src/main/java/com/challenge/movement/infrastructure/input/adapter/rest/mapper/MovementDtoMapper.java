package com.challenge.movement.infrastructure.input.adapter.rest.mapper;

import com.challenge.movement.domain.Movement;
import com.challenge.services.server.models.MovementRequestDto;
import com.challenge.services.server.models.MovementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovementDtoMapper {

  MovementResponseDto toMovementResponseDto(Movement movement);

  @Mapping(target = "movementId", ignore = true)
  Movement toMovement(MovementRequestDto movementRequestDto);
}