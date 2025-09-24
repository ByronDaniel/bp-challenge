package com.challenge.movement.infrastructure.output.adapter;

import com.challenge.movement.application.output.port.AccountOutputPort;
import com.challenge.movement.domain.Account;
import com.challenge.movement.infrastructure.output.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import task___buildSpringClient0__property__packageName_.clients0.client.AccountManagementApi;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountAdapter implements AccountOutputPort {

  AccountManagementApi accountManagementApi;
  AccountMapper accountMapper;

  @Override
  public Flux<Account> getByFilter(String accountNumber) {
    return accountManagementApi.getAll(accountNumber, null)
        .map(accountMapper::toAccount);
  }

  @Override
  public Mono<Account> getById(Integer id) {
    return accountManagementApi.getById(id)
        .map(accountMapper::toAccount);
  }

  @Override
  public Mono<Account> updateById(Integer id, Account account) {
    return accountManagementApi.updateById(id, accountMapper.toAccountRequestPutDto((account)))
        .map(accountMapper::toAccount);
  }
}
