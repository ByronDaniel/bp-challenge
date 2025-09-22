package com.challengue.client.infrastructure.input.adapter.rest.mapper;

import com.challengue.client.domain.Client;
import com.challengue.services.server.models.ClientRequestDto;
import com.challengue.services.server.models.ClientResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClientDtoMapper {

  ClientResponseDto toClientResponseDto(Client client);

  @Mapping(target = "clientId", ignore = true)
  @Mapping(target = "personId", ignore = true)
  Client toClient(ClientRequestDto clientRequestDto);
}