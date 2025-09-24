package com.challenge.account.application.service;

import static com.challenge.account.application.service.utils.Constants.ACCOUNT_NOT_FOUND;
import static com.challenge.account.application.service.utils.Constants.CLIENT_NOT_FOUND;
import static com.challenge.account.domain.Account.generateNumberAccount;

import com.challenge.account.application.input.port.AccountInputPort;
import com.challenge.account.application.output.port.AccountOutputPort;
import com.challenge.account.application.output.port.ClientOutputPort;
import com.challenge.account.domain.Account;
import com.challenge.account.infrastructure.exception.NotFoundException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements AccountInputPort {

  AccountOutputPort accountOutputPort;
  ClientOutputPort clientOutputPort;

  @Override
  public Flux<Account> getAllByFilter(String accountNumber, String clientIdentification) {
    if (Objects.nonNull(accountNumber)) {
      return findAccountByNumber(accountNumber);
    }
    if (Objects.nonNull(clientIdentification)) {
      return findAccountsByClientIdentification(clientIdentification);
    }
    return accountOutputPort.findAll();
  }

  private Flux<Account> findAccountByNumber(String accountNumber) {
    return accountOutputPort.findByNumber(accountNumber)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .flux();
  }

  private Flux<Account> findAccountsByClientIdentification(String clientIdentification) {
    return clientOutputPort.getAll(clientIdentification)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .next()
        .flatMapMany(client -> accountOutputPort.findAll()
            .filter(account -> account.getClientId().equals(client.getId())));
  }

  @Override
  public Mono<Account> getById(Integer id) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)));
  }

  @Override
  public Mono<Account> save(Account account) {
    return clientOutputPort.getAll(account.getClientIdentification())
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .next()
        .flatMap(
            client -> {
              account.setNumber(generateNumberAccount());
              account.setClientId(client.getId());
              account.setStatus(true);
              return accountOutputPort.save(account);
            }
        );
  }

  @Override
  public Mono<Account> updateById(Integer id, Account account) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .flatMap(existingAccount -> Mono.defer(() -> {
          existingAccount.setBalance(account.getBalance());
          existingAccount.setStatus(account.getStatus());
          existingAccount.setType(account.getType());
          return accountOutputPort.save(existingAccount);
        }));
  }

  @Override
  public Mono<Void> deleteById(Integer id) {
    return accountOutputPort.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .flatMap(account -> Mono.defer(() -> accountOutputPort.deleteById(id)));
  }
}