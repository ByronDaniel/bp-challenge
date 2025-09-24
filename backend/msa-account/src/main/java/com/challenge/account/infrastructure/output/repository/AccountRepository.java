package com.challenge.account.infrastructure.output.repository;

import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Integer> {

  @Query("SELECT * FROM accounts WHERE number = :number")
  Mono<AccountEntity> findByNumber(String number);
}