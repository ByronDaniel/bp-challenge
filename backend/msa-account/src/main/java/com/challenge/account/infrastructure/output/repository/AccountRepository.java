package com.challenge.account.infrastructure.output.repository;

import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Integer> {

  Flux<AccountEntity> findByClientId(Integer clientId);
}