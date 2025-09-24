package com.challenge.client.infrastructure.output.repository.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientEntity {

  @Id
  private Integer clientId;
  private Integer personId;
  private String password;
  private Boolean status;
}