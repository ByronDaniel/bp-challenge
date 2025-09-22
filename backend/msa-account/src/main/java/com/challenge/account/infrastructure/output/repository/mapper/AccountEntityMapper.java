package com.challenge.account.infrastructure.output.repository.mapper;

import com.challenge.account.domain.Account;
import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AccountEntityMapper {

  AccountEntity toAccountEntity(Account account);

  Account toAccount(AccountEntity accountEntity);
}
