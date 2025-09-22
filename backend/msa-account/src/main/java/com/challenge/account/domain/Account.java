package com.challenge.account.domain;

import java.util.UUID;
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
  Double balance;
  Boolean status;
  Integer clientId;

  public static String generateNumberAccount() {
    String hex = UUID.randomUUID().toString().replaceAll("-", "");
    long v = Math.abs(hex.substring(0, 12).hashCode());
    return String.format("%010d", v % 10_000_000_000L);
  }
}
