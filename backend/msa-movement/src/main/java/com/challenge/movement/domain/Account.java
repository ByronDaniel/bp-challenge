package com.challenge.movement.domain;

import com.challenge.movement.infrastructure.exception.ConflictException;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Account {

  Integer accountId;
  String number;
  String type;
  BigDecimal balance;

  public BigDecimal credit(BigDecimal amount) {
    this.balance = this.balance.add(amount);
    return this.balance;
  }

  public BigDecimal debit(BigDecimal amount) {
    if (this.balance.compareTo(amount) < 0) {
      throw new ConflictException("Balance not available");
    }
    this.balance = this.balance.subtract(amount);
    return this.balance;
  }
}
