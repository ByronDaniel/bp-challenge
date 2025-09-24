package com.challenge.account.application.input.port;

import com.challenge.account.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountInputPort {

  Flux<Account> getAll(String accountNumber);

  Mono<Account> getById(Integer id);

  Mono<Account> save(Account account);

  Mono<Account> updateById(Integer id, Account account);

  Mono<Void> deleteById(Integer id);
}
