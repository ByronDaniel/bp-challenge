package com.challenge.movement.infrastructure.output.repository.mapper;

import com.challenge.movement.domain.Movement;
import com.challenge.movement.infrastructure.output.repository.entity.MovementEntity;
import org.mapstruct.Mapper;

@Mapper
public interface MovementEntityMapper {

  MovementEntity toMovementEntity(Movement movement);

  Movement toMovement(MovementEntity movementEntity);
}
