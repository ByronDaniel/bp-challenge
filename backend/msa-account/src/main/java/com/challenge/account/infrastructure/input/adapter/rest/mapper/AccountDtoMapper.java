package com.challenge.account.infrastructure.input.adapter.rest.mapper;

import com.challenge.account.domain.Account;
import com.challenge.services.server.models.AccountRequestDto;
import com.challenge.services.server.models.AccountRequestPutDto;
import com.challenge.services.server.models.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDtoMapper {

  AccountResponseDto toAccountResponseDto(Account account);

  @Mapping(target = "accountId", ignore = true)
  @Mapping(target = "clientId", ignore = true)
  @Mapping(target = "number", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "clientIdentification", source = "clientIdentification")
  Account toAccount(AccountRequestDto accountRequestDto);

  @Mapping(target = "accountId", ignore = true)
  @Mapping(target = "clientId", ignore = true)
  @Mapping(target = "number", ignore = true)
  @Mapping(target = "clientIdentification", ignore = true)
  Account toAccount(AccountRequestPutDto accountRequestPutDto);
}