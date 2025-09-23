package com.challenge.report.infrastructure.output.adapter;

import com.challenge.report.application.output.port.AccountOutputPort;
import com.challenge.report.domain.Account;
import com.challenge.report.infrastructure.output.repository.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import task___buildSpringClient0__property__packageName_.clients0.client.AccountManagementApi;

@Component
@RequiredArgsConstructor
public class AccountAdapter implements AccountOutputPort {

  private final AccountManagementApi accountManagementApi;
  private final AccountMapper accountMapper;

  @Override
  public Mono<Account> getById(Integer id) {
    return accountManagementApi.getById(id)
        .map(accountMapper::toAccount);
  }

  @Override
  public Mono<Account> updateById(Integer id, Account account) {
    return accountManagementApi.updateById(id, accountMapper.toAccountRequestDto(account))
        .map(accountMapper::toAccount);
  }
}
