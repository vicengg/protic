package org.example.protic.domain;

import org.example.protic.commons.ValidationException;

import java.util.Objects;

public class UserId implements ValueObject {

  private final String value;

  private UserId(String value) {
    if (Objects.isNull(value)) {
      throw new ValidationException("Null user ID.");
    }
    this.value = value;
  }

  public static UserId of(String value) {
    return new UserId(value);
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserId userId = (UserId) o;
    return Objects.equals(value, userId.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
