package com.challenge.movement.infrastructure.output.repository.mapper;

import com.challenge.movement.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import task___buildSpringClient0__property__packageName_.clients0.client.models.AccountRequestPutDto;
import task___buildSpringClient0__property__packageName_.clients0.client.models.AccountResponseDto;

@Mapper
public interface AccountMapper {

  @Mapping(target = "clientId", ignore = true)
  Account toAccount(AccountResponseDto accountResponseDto);

  AccountRequestPutDto toAccountRequestPutDto(Account account);
}
