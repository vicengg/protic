package org.example.protic.commons;

public class ForbiddenException extends RuntimeException {

  public ForbiddenException() {
    super("Forbidden operation.");
  }
}
