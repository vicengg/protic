package org.example.protic.domain.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.text.MessageFormat;
import java.util.Objects;

public final class Company implements ValueObject {

  public static final int MAX_NAME_SIZE = 50;
  private final String name;

  private Company(String name) {
    if (StringUtils.isBlank(name)) {
      throw new ValidationException("Null or empty company name.");
    }
    if (name.length() > MAX_NAME_SIZE) {
      throw new ValidationException(
          MessageFormat.format(
              "Company name cannot be greater than {0} characters.", MAX_NAME_SIZE));
    }
    this.name = name.trim().toUpperCase();
  }

  public static Company of(String value) {
    return new Company(value);
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
    Company company = (Company) o;
    return Objects.equals(name, company.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
