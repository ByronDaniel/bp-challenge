package com.challenge.report.infrastructure.output.adapter;

import com.challenge.report.application.output.port.AccountOutputPort;
import com.challenge.report.domain.Account;
import com.challenge.report.infrastructure.output.repository.mapper.AccountMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import task___buildSpringClient1__property__packageName_.clients1.client.AccountManagementApi;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountAdapter implements AccountOutputPort {

  AccountManagementApi accountManagementApi;
  AccountMapper accountMapper;

  @Override
  public Flux<Account> getAll(String clientIdentification) {
    return accountManagementApi.getAll(null, clientIdentification)
        .map(accountMapper::toAccount);
  }

}
