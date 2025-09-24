package com.challenge.account.infrastructure.output.repository.impl;

import com.challenge.account.infrastructure.output.repository.AccountCustomRepository;
import com.challenge.account.infrastructure.output.repository.entity.AccountEntity;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

  private static final String BASE_SELECT = """
      SELECT c.account_id, c.client_id, c.number, c.type, c.balance, c.status
      FROM accounts c
      """;

  private final DatabaseClient client;

  @Override
  public Mono<AccountEntity> findByNumber(String number) {
    return client.sql(BASE_SELECT + " WHERE c.number = :number")
        .bind("number", number)
        .map((row, meta) -> {
          AccountEntity a = new AccountEntity();
          a.setAccountId(row.get("account_id", Integer.class));
          a.setClientId(row.get("client_id", Integer.class));
          a.setNumber(row.get("number", String.class));
          a.setType(row.get("type", String.class));
          BigDecimal balance = row.get("balance", BigDecimal.class);
          a.setBalance(balance != null ? balance.doubleValue() : null);
          a.setStatus(row.get("status", Boolean.class));
          return a;
        })
        .one();
  }
}
