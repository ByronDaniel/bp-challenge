package com.challenge.account.infrastructure.output.repository.mapper;

import com.challenge.account.domain.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task___buildSpringClient0__property__packageName_.clients0.client.models.ClientResponseDto;

@Mapper
public interface ClientEntityMapper {

  @Mapping(target = "id", source = "id")
  Client toClient(ClientResponseDto clientResponseDto);
}
