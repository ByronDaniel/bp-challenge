package com.challenge.movement.application.output.port;

import com.challenge.movement.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountOutputPort {

  Flux<Account> getByFilter(String accountNumber);

  Mono<Account> getById(Integer id);

  Mono<Account> updateById(Integer id, Account account);
}
