package com.challenge.movement.infrastructure.output.repository.mapper;

import com.challenge.movement.domain.Movement;
import com.challenge.movement.infrastructure.output.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovementEntityMapper {

  MovementEntity toMovementEntity(Movement movement);

  @Mapping(target = "numberAccount", ignore = true)
  Movement toMovement(MovementEntity movementEntity);
}
