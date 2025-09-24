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
        .map(this::mapRowToAccountEntity)
        .one();
  }

  private AccountEntity mapRowToAccountEntity(io.r2dbc.spi.Row row, io.r2dbc.spi.RowMetadata meta) {
    AccountEntity account = new AccountEntity();
    account.setAccountId(row.get("account_id", Integer.class));
    account.setClientId(row.get("client_id", Integer.class));
    account.setNumber(row.get("number", String.class));
    account.setType(row.get("type", String.class));
    
    BigDecimal balance = row.get("balance", BigDecimal.class);
    account.setBalance(balance != null ? balance.doubleValue() : null);
    
    account.setStatus(row.get("status", Boolean.class));
    return account;
  }
}
