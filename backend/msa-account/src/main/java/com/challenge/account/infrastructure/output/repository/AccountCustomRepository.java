package com.challenge.account.infrastructure.output.repository;

import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import reactor.core.publisher.Mono;

public interface AccountCustomRepository {

  Mono<AccountEntity> findByNumber(String number);
}
