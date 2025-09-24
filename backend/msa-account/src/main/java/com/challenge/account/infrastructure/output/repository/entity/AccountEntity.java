package com.challenge.account.infrastructure.output.repository.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "accounts")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountEntity {

  @Id
  Integer accountId;
  Integer clientId;
  @Column("number")
  String number;
  String type;
  Double balance;
  Boolean status;
}