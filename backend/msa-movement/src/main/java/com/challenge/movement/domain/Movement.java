package com.challenge.movement.domain;

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
public class Movement {

  Integer movementId;
  String date;
  String type;
  BigDecimal value;
  BigDecimal balance;
  Integer accountId;
  String numberAccount;
}
