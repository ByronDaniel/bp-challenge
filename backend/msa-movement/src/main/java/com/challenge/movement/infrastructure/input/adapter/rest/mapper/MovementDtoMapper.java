package com.challenge.movement.infrastructure.input.adapter.rest.mapper;

import com.challenge.movement.domain.Movement;
import com.challenge.services.server.models.MovementRequestDto;
import com.challenge.services.server.models.MovementRequestDto.TypeEnum;
import com.challenge.services.server.models.MovementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface MovementDtoMapper {

  @Mapping(target = "type", source = "type", qualifiedByName = "mapTypeEnum")
  MovementResponseDto toMovementResponseDto(Movement movement);

  @Mapping(target = "movementId", ignore = true)
  @Mapping(target = "type", source = "type", qualifiedByName = "mapType")
  Movement toMovement(MovementRequestDto movementRequestDto);

  @Named("mapType")
  default String mapGender(TypeEnum type) {
    return type.getValue();
  }

  @Named("mapTypeEnum")
  default MovementResponseDto.TypeEnum mapTypeEnum(String type) {
    return MovementResponseDto.TypeEnum.fromValue(type);
  }
}