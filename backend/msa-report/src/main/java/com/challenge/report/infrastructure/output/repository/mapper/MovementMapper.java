package com.challenge.report.infrastructure.output.repository.mapper;

import com.challenge.report.domain.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task___buildSpringClient2__property__packageName_.clients2.client.models.MovementResponseDto;

@Mapper
public interface MovementMapper {

  @Mapping(target = "accountId", ignore = true)
  Movement toMovement(MovementResponseDto movementResponseDto);
}
