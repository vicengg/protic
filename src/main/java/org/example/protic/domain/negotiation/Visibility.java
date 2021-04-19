package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;

import java.util.Arrays;

public enum Visibility {
  ALREADY_VISIBLE,
  MAKE_PUBLIC,
  KEEP_PRIVATE;

  public static Visibility of(String code) {
    return Arrays.stream(Visibility.values())
        .filter(c -> c.name().equalsIgnoreCase(code))
        .findFirst()
        .orElseThrow(() -> new ValidationException("Invalid visibility code."));
  }
}
