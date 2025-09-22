package com.challengue.client.infrastructure.output.repository.entity;

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
  @Column("id")
  private Integer clientId;
  private String password;
  private Boolean status;

  @Column("personId")
  private Integer personId;
}