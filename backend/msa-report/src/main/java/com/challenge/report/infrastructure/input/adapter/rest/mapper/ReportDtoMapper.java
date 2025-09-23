package com.challenge.report.infrastructure.input.adapter.rest.mapper;

import com.challenge.report.domain.Report;
import com.challenge.services.server.models.ReportResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReportDtoMapper {

  @Mapping(target = "date", source = "movement.date")
  @Mapping(target = "name", source = "client.name")
  @Mapping(target = "number", source = "account.number")
  @Mapping(target = "typeAccount", source = "account.type")
  @Mapping(target = "status", source = "account.status")
  @Mapping(target = "type", source = "movement.type")
  @Mapping(target = "initialBalance", source = "account.balance")
  @Mapping(target = "value", source = "movement.value")
  @Mapping(target = "balance", source = "movement.balance")
  ReportResponseDto toReportResponseDto(Report report);
}