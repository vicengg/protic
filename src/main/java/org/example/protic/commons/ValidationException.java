package org.example.protic.commons;

import java.util.Objects;

public final class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(Objects.requireNonNull(message, "Null validation exception message."));
  }
}
