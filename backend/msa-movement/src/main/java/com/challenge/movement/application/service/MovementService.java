package com.challenge.movement.application.service;

import static com.challenge.movement.application.service.utils.Constants.ACCOUNT_NOT_FOUND;
import static com.challenge.movement.application.service.utils.Constants.CREDITO;

import com.challenge.movement.application.input.port.MovementInputPort;
import com.challenge.movement.application.output.port.AccountOutputPort;
import com.challenge.movement.application.output.port.MovementOutputPort;
import com.challenge.movement.domain.Account;
import com.challenge.movement.domain.Movement;
import com.challenge.movement.infrastructure.exception.NotFoundException;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovementService implements MovementInputPort {

  MovementOutputPort movementOutputPort;
  AccountOutputPort accountOutputPort;

  @Override
  public Flux<Movement> getAllByOptionalFilter(String numberAccount) {
    if (!Objects.isNull(numberAccount)) {
      return accountOutputPort.getByFilter(numberAccount)
          .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
          .flatMap(account -> movementOutputPort.findByAccountId(account.getAccountId()))
          .flatMap(movement -> accountOutputPort.getById(movement.getAccountId())
              .map(account -> {
                movement.setNumberAccount(account.getNumber());
                return movement;
              }));
    } else {
      return movementOutputPort.findAll()
          .flatMap(movement -> accountOutputPort.getById(movement.getAccountId())
              .map(account -> {
                movement.setNumberAccount(account.getNumber());
                return movement;
              }));
    }
  }

  @Override
  public Mono<Movement> save(Movement movement) {
    return accountOutputPort.getByFilter(movement.getNumberAccount())
        .switchIfEmpty(Mono.error(new NotFoundException(ACCOUNT_NOT_FOUND)))
        .next()
        .flatMap(account -> {
          BigDecimal newBalance = movement.getType().equals(CREDITO)
              ? account.credit(movement.getValue())
              : account.debit(movement.getValue());

          return persistMovementAndUpdateAccount(movement, account, newBalance);
        });
  }

  private Mono<Movement> persistMovementAndUpdateAccount(
      Movement movement, Account account, BigDecimal newBalance) {

    movement.setBalance(newBalance);
    account.setBalance(newBalance);
    movement.setAccountId(account.getAccountId());
    return movementOutputPort.save(movement)
        .flatMap(savedMovement ->
            accountOutputPort.updateById(account.getAccountId(), account)
                .thenReturn(savedMovement)
        );
  }
}