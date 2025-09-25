package com.challenge.account.application.output.port;

import com.challenge.account.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountOutputPort {

  Flux<Account> findAll();
  
  Mono<Account> findById(Integer id);

  Mono<Account> save(Account account);

  Mono<Void> deleteById(Integer id);
}
