package com.challenge.report.infrastructure.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }
} 