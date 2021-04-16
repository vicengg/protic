package org.example.protic.domain.workexperience;

import org.apache.commons.collections4.CollectionUtils;
import org.example.protic.commons.CurrencyContext;
import org.example.protic.commons.ForbiddenException;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;
import org.example.protic.domain.UserId;
import org.javamoney.moneta.Money;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class WorkExperienceEntity extends Entity implements WorkExperience {

  private final UserId userId;
  private final boolean binding;
  private final WorkExperienceField<JobTitle> jobTitle;
  private final WorkExperienceField<Company> company;
  private final WorkExperienceField<Set<Technology>> technologies;
  private final WorkExperienceField<WorkPeriod> workPeriod;
  private final WorkExperienceField<Money> salary;

  private WorkExperienceEntity(Builder builder) {
    super(Objects.requireNonNull(builder.id), Objects.requireNonNull(builder.createdAt));
    this.userId =
        Optional.ofNullable(builder.userId)
            .orElseThrow(
                () -> new ValidationException("User ID is mandatory for work experience."));
    this.binding = builder.binding;
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
    this.salary =
        Optional.ofNullable(builder.salary)
            .orElseThrow(() -> new ValidationException("Salary is mandatory for work experience."));
    if (!CurrencyContext.isAllowed(salary.getValue().getCurrency())) {
      throw new ValidationException(
          MessageFormat.format("Not valid currency: {0}", salary.getValue().getCurrency()));
    }
  }

  private WorkExperienceEntity(WorkExperience workExperience) {
    super(workExperience.getId(), workExperience.getCreatedAt());
    this.userId = workExperience.getUserId();
    this.binding = workExperience.getBinding();
    this.jobTitle = workExperience.getJobTitle();
    this.company = workExperience.getCompany();
    this.technologies = workExperience.getTechnologies();
    this.workPeriod = workExperience.getWorkPeriod();
    this.salary = workExperience.getSalary();
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

  public WorkExperienceProjection toWorkExperienceResponse(UserId userId) {
    Objects.requireNonNull(userId, "Null input user ID.");
    WorkExperienceProjectionImpl workExperienceProjection = new WorkExperienceProjectionImpl();
    workExperienceProjection.id = this.getId();
    if (this.userId.equals(userId) || this.binding) {
      workExperienceProjection.userId = this.userId;
    }
    if (this.userId.equals(userId) || this.jobTitle.isPublic()) {
      workExperienceProjection.jobTitle = this.jobTitle.getValue();
    }
    if (this.userId.equals(userId) || this.company.isPublic()) {
      workExperienceProjection.company = this.company.getValue();
    }
    if (this.userId.equals(userId) || this.technologies.isPublic()) {
      workExperienceProjection.technologies = this.technologies.getValue();
    }
    if (this.userId.equals(userId) || this.workPeriod.isPublic()) {
      workExperienceProjection.workPeriod = this.workPeriod.getValue();
    }
    if (this.userId.equals(userId) || this.salary.isPublic()) {
      workExperienceProjection.salary = this.salary.getValue();
    }
    return workExperienceProjection;
  }

  public static WorkExperienceEntity copy(WorkExperience workExperience) {
    return new WorkExperienceEntity(workExperience);
  }

  public Builder update(UserId userId) {
    if (!this.userId.equals(userId)) {
      throw new ForbiddenException();
    }
    return new Builder(this.getId(), this.getCreatedAt());
  }

  public static Builder builder() {
    return new Builder(UUID.randomUUID(), Timestamp.from(Instant.now()));
  }

  public static final class Builder {
    private final UUID id;
    private final Timestamp createdAt;
    private UserId userId;
    private boolean binding;
    public WorkExperienceField<Money> salary;
    private WorkExperienceField<JobTitle> jobTitle;
    private WorkExperienceField<Company> company;
    private WorkExperienceField<Set<Technology>> technologies;
    private WorkExperienceField<WorkPeriod> workPeriod;

    public Builder(UUID id, Timestamp createdAt) {
      this.id = id;
      this.createdAt = createdAt;
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

    public WorkExperienceEntity build() {
      return new WorkExperienceEntity(this);
    }
  }

  private static final class WorkExperienceProjectionImpl implements WorkExperienceProjection {

    private UUID id;
    private UserId userId;
    private JobTitle jobTitle;
    private Company company;
    private Set<Technology> technologies;
    private WorkPeriod workPeriod;
    private Money salary;

    @Override
    public UUID getId() {
      return id;
    }

    @Override
    public Optional<UserId> getUserId() {
      return Optional.ofNullable(userId);
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

    @Override
    public Optional<Money> getSalary() {
      return Optional.ofNullable(salary);
    }
  }
}
