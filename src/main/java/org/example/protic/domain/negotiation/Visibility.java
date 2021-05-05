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

  public static Visibility getMostPermissive(Visibility visibility1, Visibility visibility2) {
    if (visibility1 == ALREADY_VISIBLE || visibility2 == ALREADY_VISIBLE) {
      return ALREADY_VISIBLE;
    } else if (visibility1 == MAKE_PUBLIC || visibility2 == MAKE_PUBLIC) {
      return MAKE_PUBLIC;
    } else {
      return KEEP_PRIVATE;
    }
  }
}
