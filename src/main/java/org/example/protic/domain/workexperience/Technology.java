package org.example.protic.domain.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.text.MessageFormat;
import java.util.Objects;

public final class Technology implements ValueObject {

  public static final int MAX_NAME_SIZE = 50;
  private final String name;

  private Technology(String name) {
    if (StringUtils.isBlank(name)) {
      throw new ValidationException("Null or empty technology name.");
    }
    if (name.length() > MAX_NAME_SIZE) {
      throw new ValidationException(
          MessageFormat.format(
              "Technology name cannot be greater than {0} characters.", MAX_NAME_SIZE));
    }
    this.name = name.trim().toUpperCase();
  }

  public static Technology of(String value) {
    return new Technology(value);
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Technology that = (Technology) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
