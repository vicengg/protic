package org.example.protic.commons;

import java.util.Objects;

public final class UnexpectedException extends RuntimeException {

  public UnexpectedException(String message) {
    super(Objects.requireNonNull(message, "Null unexpected exception message."));
  }
}
