package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.util.Optional;
import java.util.UUID;

public final class VisibilityRequest implements ValueObject {

  private final UUID workExperienceId;
  private final Visibility jobTitle;
  private final Visibility company;
  private final Visibility technologies;
  private final Visibility workPeriod;
  private final Visibility salary;

  private VisibilityRequest(Builder builder) {
    this.workExperienceId =
        Optional.ofNullable(builder.workExperienceId)
            .orElseThrow(
                () ->
                    new ValidationException(
                        "Work experience ID is required for visibility requests."));
    this.jobTitle = Optional.ofNullable(builder.jobTitle).orElse(Visibility.KEEP_PRIVATE);
    this.company = Optional.ofNullable(builder.company).orElse(Visibility.KEEP_PRIVATE);
    this.technologies = Optional.ofNullable(builder.technologies).orElse(Visibility.KEEP_PRIVATE);
    this.workPeriod = Optional.ofNullable(builder.workPeriod).orElse(Visibility.KEEP_PRIVATE);
    this.salary = Optional.ofNullable(builder.salary).orElse(Visibility.KEEP_PRIVATE);
  }

  public UUID getWorkExperienceId() {
    return workExperienceId;
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

  public static Builder builder(UUID workExperienceId) {
    return new Builder(workExperienceId);
  }

  public static final class Builder {

    private final UUID workExperienceId;
    private Visibility jobTitle;
    private Visibility company;
    private Visibility technologies;
    private Visibility workPeriod;
    private Visibility salary;

    private Builder(UUID workExperienceId) {
      this.workExperienceId = workExperienceId;
    }

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
}
