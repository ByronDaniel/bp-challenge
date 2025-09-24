package com.challenge.report.infrastructure.output.repository.mapper;

import com.challenge.report.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task___buildSpringClient1__property__packageName_.clients1.client.models.AccountResponseDto;

@Mapper
public interface AccountMapper {

  @Mapping(target = "id", source = "accountId")
  @Mapping(target = "clientId", ignore = true)
  Account toAccount(AccountResponseDto accountResponseDto);

}
