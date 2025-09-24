package com.challenge.movement.infrastructure.output.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "movements")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MovementEntity {

  @Id
  Integer movementId;
  LocalDateTime date;
  String type;
  BigDecimal value;
  BigDecimal balance;
  Integer accountId;
}