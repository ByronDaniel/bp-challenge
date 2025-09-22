package com.challenge.account.application.service;

import static com.challenge.account.application.service.utils.Constants.ACCOUNT_NOT_FOUND;
import static com.challenge.account.domain.Account.generateNumberAccount;

import com.challenge.account.application.input.port.AccountInputPort;
import com.challenge.account.application.output.port.AccountOutputPort;
import com.challenge.account.domain.Account;
import com.challenge.account.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountInputPort {

  private final AccountOutputPort accountOutputPort;

  @Override
  public Flux<Account> getAll() {
    return accountOutputPort.findAll();
  }

  @Override
  public Mono<Account> getById(Integer id) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)));
  }

  @Override
  public Mono<Account> save(Account account) {
    account.setNumber(generateNumberAccount());
    return accountOutputPort.save(account);
  }

  @Override
  public Mono<Account> updateById(Integer id, Account account) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .flatMap(existingAccount -> {
          existingAccount.setBalance(account.getBalance());
          existingAccount.setStatus(account.getStatus());
          return accountOutputPort.save(existingAccount);
        });
  }

  @Override
  public Mono<Void> deleteById(Integer id) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .flatMap(account -> accountOutputPort.deleteById(id));
  }
}