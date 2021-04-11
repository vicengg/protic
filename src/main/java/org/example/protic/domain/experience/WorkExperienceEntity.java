package org.example.protic.domain.experience;

import org.apache.commons.collections4.CollectionUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;

import java.util.Optional;
import java.util.Set;

public final class WorkExperienceEntity extends Entity implements WorkExperience {

  private final WorkExperienceField<JobTitle> jobTitle;
  private final WorkExperienceField<Company> company;
  private final WorkExperienceField<Set<Technology>> technologies;
  private final WorkExperienceField<WorkPeriod> workPeriod;

  private WorkExperienceEntity(Builder builder) {
    this.jobTitle =
        Optional.ofNullable(builder.jobTitle)
            .orElseThrow(
                () -> new ValidationException("Job title is mandatory for work experience."));
    this.company =
        Optional.ofNullable(builder.company)
            .orElseThrow(
                () -> new ValidationException("Company is mandatory for work experience."));
    this.technologies =
        Optional.ofNullable(builder.technologies)
            .orElseThrow(
                () ->
                    new ValidationException(
                        "A list of technologies is mandatory for work experience."));
    if (CollectionUtils.isEmpty(this.technologies.getValue())) {
      throw new ValidationException("The technologies list must contain at least one element.");
    }
    this.workPeriod =
        Optional.ofNullable(builder.workPeriod)
            .orElseThrow(
                () -> new ValidationException("Work period is mandatory for work experience."));
  }

  public WorkExperienceField<JobTitle> getJobTitle() {
    return jobTitle;
  }

  public WorkExperienceField<Company> getCompany() {
    return company;
  }

  public WorkExperienceField<Set<Technology>> getTechnologies() {
    return technologies;
  }

  public WorkExperienceField<WorkPeriod> getWorkPeriod() {
    return workPeriod;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private WorkExperienceField<JobTitle> jobTitle;
    private WorkExperienceField<Company> company;
    private WorkExperienceField<Set<Technology>> technologies;
    private WorkExperienceField<WorkPeriod> workPeriod;

    private Builder() {}

    public Builder withJobTitle(WorkExperienceField<JobTitle> jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder withCompany(WorkExperienceField<Company> company) {
      this.company = company;
      return this;
    }

    public Builder withTechnologies(WorkExperienceField<Set<Technology>> technologies) {
      this.technologies = technologies;
      return this;
    }

    public Builder withWorkPeriod(WorkExperienceField<WorkPeriod> workPeriod) {
      this.workPeriod = workPeriod;
      return this;
    }

    public WorkExperienceEntity build() {
      return new WorkExperienceEntity(this);
    }
  }
}
