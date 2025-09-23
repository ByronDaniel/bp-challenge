package com.challenge.report.application.output.port;

import com.challenge.report.domain.Account;
import reactor.core.publisher.Flux;

public interface AccountOutputPort {

  Flux<Account> getAll(Integer clientId);
}
