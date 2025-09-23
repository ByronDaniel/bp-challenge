package com.challenge.report.infrastructure.output.repository.mapper;

import com.challenge.report.domain.Account;
import org.mapstruct.Mapper;
import task___buildSpringClient0__property__packageName_.clients0.client.models.AccountRequestDto;
import task___buildSpringClient0__property__packageName_.clients0.client.models.AccountResponseDto;

@Mapper
public interface AccountMapper {

  Account toAccount(AccountResponseDto accountResponseDto);

  AccountRequestDto toAccountRequestDto(Account account);
}
