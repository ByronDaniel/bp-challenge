package com.challenge.client.infrastructure.output.repository.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "persons")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonEntity {

  @Id
  Integer personId;
  String name;
  String gender;
  Integer age;
  String identification;
  String address;
  String phone;
}
