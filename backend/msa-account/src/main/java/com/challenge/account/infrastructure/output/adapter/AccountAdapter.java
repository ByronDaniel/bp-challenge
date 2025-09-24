package com.challenge.account.infrastructure.output.adapter;

import com.challenge.account.application.output.port.AccountOutputPort;
import com.challenge.account.domain.Account;
import com.challenge.account.infrastructure.output.repository.AccountCustomRepository;
import com.challenge.account.infrastructure.output.repository.AccountRepository;
import com.challenge.account.infrastructure.output.repository.mapper.AccountEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountAdapter implements AccountOutputPort {

  AccountEntityMapper accountEntityMapper;
  AccountRepository accountRepository;
  AccountCustomRepository accountCustomRepository;

  @Override
  public Flux<Account> findAll() {
    return accountRepository.findAll()
        .map(accountEntityMapper::toAccount);
  }

  @Override
  public Mono<Account> findByNumber(String number) {
    return accountCustomRepository.findByNumber(number)
        .map(accountEntityMapper::toAccount);
  }

  @Override
  public Mono<Account> findById(Integer id) {
    return accountRepository.findById(id)
        .map(accountEntityMapper::toAccount);
  }

  @Override
  public Mono<Account> save(Account account) {
    return accountRepository.save(accountEntityMapper.toAccountEntity(account))
        .map(accountEntityMapper::toAccount);
  }

  @Override
  public Mono<Void> deleteById(Integer id) {
    return accountRepository.deleteById(id);
  }
}
