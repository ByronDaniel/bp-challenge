package com.challenge.report.application.output.port;

import com.challenge.report.domain.Account;
import reactor.core.publisher.Mono;

public interface AccountOutputPort {

  Mono<Account> getById(Integer id);

  Mono<Account> updateById(Integer id, Account account);
}
