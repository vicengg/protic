package org.example.protic.infrastructure.database;

import org.example.protic.domain.workexperience.*;

import java.util.Optional;
import java.util.Set;

public final class FilteredWorkExperienceImpl implements FilteredWorkExperience {

  private final JobTitle jobTitle;
  private final Company company;
  private final Set<Technology> technologies;
  private final WorkPeriod workPeriod;

  private FilteredWorkExperienceImpl(Builder builder) {
    this.jobTitle = builder.jobTitle;
    this.company = builder.company;
    this.technologies = builder.technologies;
    this.workPeriod = builder.workPeriod;
  }

  @Override
  public Optional<JobTitle> getJobTitle() {
    return Optional.ofNullable(jobTitle);
  }

  @Override
  public Optional<Company> getCompany() {
    return Optional.ofNullable(company);
  }

  @Override
  public Optional<Set<Technology>> getTechnologies() {
    return Optional.ofNullable(technologies);
  }

  @Override
  public Optional<WorkPeriod> getWorkPeriod() {
    return Optional.ofNullable(workPeriod);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {

    private JobTitle jobTitle;
    private Company company;
    private Set<Technology> technologies;
    private WorkPeriod workPeriod;

    public Builder withJobTitle(JobTitle jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder withCompany(Company company) {
      this.company = company;
      return this;
    }

    public Builder withTechnologies(Set<Technology> technologies) {
      this.technologies = technologies;
      return this;
    }

    public Builder withWorkPeriod(WorkPeriod workPeriod) {
      this.workPeriod = workPeriod;
      return this;
    }

    public FilteredWorkExperienceImpl build() {
      return new FilteredWorkExperienceImpl(this);
    }
  }
}
