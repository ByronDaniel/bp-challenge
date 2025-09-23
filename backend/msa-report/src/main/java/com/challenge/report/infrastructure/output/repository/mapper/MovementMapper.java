package com.challenge.report.infrastructure.output.repository.mapper;

import com.challenge.report.domain.Client;
import org.mapstruct.Mapper;
import task___buildSpringClient0__property__packageName_.clients0.client.models.ClientResponseDto;

@Mapper
public interface ClientMapper {

  Client toClient(ClientResponseDto clientResponseDto);
}
