package org.example.protic.domain.user;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.util.Objects;

public class UserId implements ValueObject {

  private final String value;

  private UserId(String value) {
    if (StringUtils.isBlank(value)) {
      throw new ValidationException("Null or empty user ID.");
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
