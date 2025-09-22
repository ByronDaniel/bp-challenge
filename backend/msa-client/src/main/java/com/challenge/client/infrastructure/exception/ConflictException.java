package com.challenge.client.infrastructure.exception;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }
} 