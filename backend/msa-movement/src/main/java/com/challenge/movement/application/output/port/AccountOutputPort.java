package com.challenge.movement.application.output.port;

import com.challenge.movement.domain.Account;
import reactor.core.publisher.Mono;

public interface AccountOutputPort {

  Mono<Account> getById(Integer id);

  Mono<Account> updateById(Integer id, Account account);
}
