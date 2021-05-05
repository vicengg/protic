package org.example.protic.domain.negotiation;

import org.example.protic.domain.ValueObject;

import java.util.Objects;
import java.util.Optional;

public final class VisibilityRequest implements ValueObject {

  private final Visibility jobTitle;
  private final Visibility company;
  private final Visibility technologies;
  private final Visibility workPeriod;
  private final Visibility salary;

  private VisibilityRequest(Builder builder) {
    this.jobTitle = Optional.ofNullable(builder.jobTitle).orElse(Visibility.KEEP_PRIVATE);
    this.company = Optional.ofNullable(builder.company).orElse(Visibility.KEEP_PRIVATE);
    this.technologies = Optional.ofNullable(builder.technologies).orElse(Visibility.KEEP_PRIVATE);
    this.workPeriod = Optional.ofNullable(builder.workPeriod).orElse(Visibility.KEEP_PRIVATE);
    this.salary = Optional.ofNullable(builder.salary).orElse(Visibility.KEEP_PRIVATE);
  }

  public Visibility getJobTitle() {
    return jobTitle;
  }

  public Visibility getCompany() {
    return company;
  }

  public Visibility getTechnologies() {
    return technologies;
  }

  public Visibility getWorkPeriod() {
    return workPeriod;
  }

  public Visibility getSalary() {
    return salary;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private Visibility jobTitle;
    private Visibility company;
    private Visibility technologies;
    private Visibility workPeriod;
    private Visibility salary;

    private Builder() {}

    public Builder withJobTitle(Visibility jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder withCompany(Visibility company) {
      this.company = company;
      return this;
    }

    public Builder withTechnologies(Visibility technologies) {
      this.technologies = technologies;
      return this;
    }

    public Builder withWorkPeriod(Visibility workPeriod) {
      this.workPeriod = workPeriod;
      return this;
    }

    public Builder withSalary(Visibility salary) {
      this.salary = salary;
      return this;
    }

    public VisibilityRequest build() {
      return new VisibilityRequest(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VisibilityRequest that = (VisibilityRequest) o;
    return jobTitle == that.jobTitle
        && company == that.company
        && technologies == that.technologies
        && workPeriod == that.workPeriod
        && salary == that.salary;
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobTitle, company, technologies, workPeriod, salary);
  }
}
