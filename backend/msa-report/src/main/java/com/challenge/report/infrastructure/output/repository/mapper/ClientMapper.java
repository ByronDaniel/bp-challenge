package com.challenge.report.infrastructure.output.repository.mapper;

import com.challenge.report.domain.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task___buildSpringClient0__property__packageName_.clients0.client.models.ClientResponseDto;

@Mapper
public interface ClientMapper {

  @Mapping(target = "clientId", source = "id")
  Client toClient(ClientResponseDto clientResponseDto);
}
