package com.challenge.client.infrastructure.input.adapter.rest.mapper;

import com.challenge.client.domain.Client;
import com.challenge.services.server.models.ClientRequestDto;
import com.challenge.services.server.models.ClientResponseDto;
import com.challenge.services.server.models.ClientResponseDto.GenderEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ClientDtoMapper {

  @Mapping(target = "id", source = "clientId")
  @Mapping(target = "gender", source = "gender", qualifiedByName = "mapGenderEnum")
  ClientResponseDto toClientResponseDto(Client client);

  @Mapping(target = "clientId", ignore = true)
  @Mapping(target = "personId", ignore = true)
  Client toClient(ClientRequestDto clientRequestDto);


  @Named("mapGenderEnum")
  default GenderEnum mapGenderEnum(String gender) {
    return GenderEnum.fromValue(gender);
  }
}