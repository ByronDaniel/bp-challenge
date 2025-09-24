package com.challenge.account.infrastructure.output.repository.mapper;

import com.challenge.account.domain.Account;
import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountEntityMapper {

  AccountEntity toAccountEntity(Account account);

  @Mapping(target = "clientIdentification", ignore = true)
  Account toAccount(AccountEntity accountEntity);
}
