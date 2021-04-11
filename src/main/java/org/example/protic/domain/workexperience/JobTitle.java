package org.example.protic.domain.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.text.MessageFormat;
import java.util.Objects;

public final class JobTitle implements ValueObject {

  public static final int MAX_NAME_SIZE = 50;
  private final String name;

  private JobTitle(String name) {
    if (StringUtils.isBlank(name)) {
      throw new ValidationException("Null or empty job title name.");
    }
    if (name.length() > MAX_NAME_SIZE) {
      throw new ValidationException(
          MessageFormat.format(
              "Job title name cannot be greater than {0} characters.", MAX_NAME_SIZE));
    }
    this.name = name.trim().toUpperCase();
  }

  public static JobTitle of(String value) {
    return new JobTitle(value);
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
    JobTitle jobTitle = (JobTitle) o;
    return Objects.equals(name, jobTitle.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
