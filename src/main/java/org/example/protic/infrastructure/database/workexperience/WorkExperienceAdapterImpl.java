package org.example.protic.infrastructure.database.workexperience;

import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public class WorkExperienceAdapterImpl implements WorkExperience {

  private final UUID id;
  private final Timestamp createdAt;
  private final UserId userId;
  private final boolean binding;
  private final WorkExperienceField<JobTitle> jobTitle;
  private final WorkExperienceField<Company> company;
  private final WorkExperienceField<Set<Technology>> technologies;
  private final WorkExperienceField<WorkPeriod> workPeriod;
  private final WorkExperienceField<Money> salary;

  private WorkExperienceAdapterImpl(Builder builder) {
    this.id = builder.id;
    this.createdAt = builder.createdAt;
    this.userId = builder.userId;
    this.binding = builder.binding;
    this.jobTitle = builder.jobTitle;
    this.company = builder.company;
    this.technologies = builder.technologies;
    this.workPeriod = builder.workPeriod;
    this.salary = builder.salary;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  @Override
  public UserId getUserId() {
    return userId;
  }

  @Override
  public boolean getBinding() {
    return binding;
  }

  @Override
  public WorkExperienceField<JobTitle> getJobTitle() {
    return jobTitle;
  }

  @Override
  public WorkExperienceField<Company> getCompany() {
    return company;
  }

  @Override
  public WorkExperienceField<Set<Technology>> getTechnologies() {
    return technologies;
  }

  @Override
  public WorkExperienceField<WorkPeriod> getWorkPeriod() {
    return workPeriod;
  }

  @Override
  public WorkExperienceField<Money> getSalary() {
    return salary;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private UUID id;
    private Timestamp createdAt;
    private UserId userId;
    private boolean binding;
    private WorkExperienceField<JobTitle> jobTitle;
    private WorkExperienceField<Company> company;
    private WorkExperienceField<Set<Technology>> technologies;
    private WorkExperienceField<WorkPeriod> workPeriod;
    private WorkExperienceField<Money> salary;

    public Builder withId(UUID id) {
      this.id = id;
      return this;
    }

    public Builder withCreatedAt(Timestamp createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder withUserId(UserId userId) {
      this.userId = userId;
      return this;
    }

    public Builder withBinding(boolean binding) {
      this.binding = binding;
      return this;
    }

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

    public Builder withSalary(WorkExperienceField<Money> salary) {
      this.salary = salary;
      return this;
    }

    public WorkExperienceAdapterImpl build() {
      return new WorkExperienceAdapterImpl(this);
    }
  }
}
