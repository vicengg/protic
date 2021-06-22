package org.example.protic.domain.workexperience;

import org.apache.commons.collections4.CollectionUtils;
import org.example.protic.commons.CurrencyContext;
import org.example.protic.commons.ForbiddenException;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;
import org.example.protic.domain.negotiation.Action;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.domain.user.User;
import org.javamoney.moneta.Money;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

public final class WorkExperienceEntity extends Entity implements WorkExperience {

  private final User user;
  private final boolean binding;
  private final RestrictedField<JobTitle> jobTitle;
  private final RestrictedField<Company> company;
  private final RestrictedField<Set<Technology>> technologies;
  private final RestrictedField<WorkPeriod> workPeriod;
  private final RestrictedField<Money> salary;

  private WorkExperienceEntity(Builder builder) {
    super(Objects.requireNonNull(builder.id), Objects.requireNonNull(builder.createdAt));
    this.user =
        Optional.ofNullable(builder.user)
            .orElseThrow(() -> new ValidationException("User is mandatory for work experience."));
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
    this.user = workExperience.getUser();
    this.binding = workExperience.getBinding();
    this.jobTitle = workExperience.getJobTitle();
    this.company = workExperience.getCompany();
    this.technologies = workExperience.getTechnologies();
    this.workPeriod = workExperience.getWorkPeriod();
    this.salary = workExperience.getSalary();
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public boolean getBinding() {
    return binding;
  }

  @Override
  public RestrictedField<JobTitle> getJobTitle() {
    return jobTitle;
  }

  @Override
  public RestrictedField<Company> getCompany() {
    return company;
  }

  @Override
  public RestrictedField<Set<Technology>> getTechnologies() {
    return technologies;
  }

  @Override
  public RestrictedField<WorkPeriod> getWorkPeriod() {
    return workPeriod;
  }

  @Override
  public RestrictedField<Money> getSalary() {
    return salary;
  }

  public WorkExperienceProjection toWorkExperienceProjection(
      User user, List<Negotiation> negotiations) {
    Objects.requireNonNull(user, "Null input user ID.");
    WorkExperienceProjectionImpl workExperienceProjection = new WorkExperienceProjectionImpl();
    workExperienceProjection.id = this.getId();
    workExperienceProjection.createdAt = this.getCreatedAt();
    workExperienceProjection.binding = this.getBinding();
    if (this.user.getId().equals(user.getId()) || this.binding) {
      workExperienceProjection.user = this.user;
    }
    workExperienceProjection.jobTitle =
        controlVisibility(user, jobTitle, negotiations, VisibilityRequest::getJobTitle);
    workExperienceProjection.company =
        controlVisibility(user, company, negotiations, VisibilityRequest::getCompany);
    workExperienceProjection.technologies =
        controlVisibility(user, technologies, negotiations, VisibilityRequest::getTechnologies);
    workExperienceProjection.workPeriod =
        controlVisibility(user, workPeriod, negotiations, VisibilityRequest::getWorkPeriod);
    workExperienceProjection.salary =
        controlVisibility(user, salary, negotiations, VisibilityRequest::getSalary);
    return workExperienceProjection;
  }

  private <S, T extends RestrictedField<S>> T controlVisibility(
      User user,
      T restrictedField,
      List<Negotiation> negotiations,
      Function<VisibilityRequest, Visibility> visibilityFieldAccess) {
    boolean isOwner = this.user.getId().equals(user.getId());
    if (isOwner || restrictedField.isPublic()) {
      return restrictedField;
    } else {
      if (getVisibilityFlag(user, negotiations, visibilityFieldAccess) == Visibility.MAKE_PUBLIC
          || getVisibilityFlag(user, negotiations, visibilityFieldAccess)
              == Visibility.ALREADY_VISIBLE) {
        return restrictedField;
      }
    }
    return null;
  }

  private Visibility getVisibilityFlag(
      User user,
      List<Negotiation> negotiations,
      Function<VisibilityRequest, Visibility> visibilityFieldAccess) {
    return Visibility.getMostPermissive(
        negotiations.stream()
            .filter(Negotiation::isAccepted)
            .filter(negotiation -> negotiation.getCreator().getId().equals(user.getId()))
            .filter(
                negotiation -> negotiation.getDemandedWorkExperience().getId().equals(this.getId()))
            .map(Negotiation::getActions)
            .flatMap(Collection::stream)
            .map(Action::getDemandedVisibility)
            .map(visibilityFieldAccess)
            .reduce(Visibility.KEEP_PRIVATE, Visibility::getMostPermissive),
        negotiations.stream()
            .filter(Negotiation::isAccepted)
            .filter(negotiation -> negotiation.getReceiver().getId().equals(user.getId()))
            .filter(
                negotiation -> negotiation.getOfferedWorkExperience().getId().equals(this.getId()))
            .map(Negotiation::getActions)
            .flatMap(Collection::stream)
            .map(Action::getOfferedVisibility)
            .map(visibilityFieldAccess)
            .reduce(Visibility.KEEP_PRIVATE, Visibility::getMostPermissive));
  }

  public static WorkExperienceEntity copy(WorkExperience workExperience) {
    return new WorkExperienceEntity(workExperience);
  }

  public Builder update(User user) {
    if (!this.user.getId().equals(user.getId())) {
      throw new ForbiddenException();
    } else {
      return new Builder(this.getId(), this.getCreatedAt());
    }
  }

  public UUID checkForDelete(User user) {
    if (!this.user.getId().equals(user.getId())) {
      throw new ForbiddenException();
    } else {
      return getId();
    }
  }

  public static Builder builder() {
    return new Builder(UUID.randomUUID(), Timestamp.from(Instant.now()));
  }

  public static final class Builder {
    private final UUID id;
    private final Timestamp createdAt;
    private User user;
    private boolean binding;
    public RestrictedField<Money> salary;
    private RestrictedField<JobTitle> jobTitle;
    private RestrictedField<Company> company;
    private RestrictedField<Set<Technology>> technologies;
    private RestrictedField<WorkPeriod> workPeriod;

    private Builder(UUID id, Timestamp createdAt) {
      this.id = id;
      this.createdAt = createdAt;
    }

    public Builder withUser(User user) {
      this.user = user;
      return this;
    }

    public Builder withBinding(boolean binding) {
      this.binding = binding;
      return this;
    }

    public Builder withJobTitle(RestrictedField<JobTitle> jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder withCompany(RestrictedField<Company> company) {
      this.company = company;
      return this;
    }

    public Builder withTechnologies(RestrictedField<Set<Technology>> technologies) {
      this.technologies = technologies;
      return this;
    }

    public Builder withWorkPeriod(RestrictedField<WorkPeriod> workPeriod) {
      this.workPeriod = workPeriod;
      return this;
    }

    public Builder withSalary(RestrictedField<Money> salary) {
      this.salary = salary;
      return this;
    }

    public WorkExperienceEntity build() {
      return new WorkExperienceEntity(this);
    }
  }

  private static final class WorkExperienceProjectionImpl implements WorkExperienceProjection {

    private UUID id;
    private Timestamp createdAt;
    private User user;
    private boolean binding;
    private RestrictedField<JobTitle> jobTitle;
    private RestrictedField<Company> company;
    private RestrictedField<Set<Technology>> technologies;
    private RestrictedField<WorkPeriod> workPeriod;
    private RestrictedField<Money> salary;

    @Override
    public UUID getId() {
      return id;
    }

    @Override
    public boolean isBound() {
      return binding;
    }

    @Override
    public Timestamp getCreatedAt() {
      return createdAt;
    }

    @Override
    public Optional<User> getUser() {
      return Optional.ofNullable(user);
    }

    @Override
    public Optional<RestrictedField<JobTitle>> getJobTitle() {
      return Optional.ofNullable(jobTitle);
    }

    @Override
    public Optional<RestrictedField<Company>> getCompany() {
      return Optional.ofNullable(company);
    }

    @Override
    public Optional<RestrictedField<Set<Technology>>> getTechnologies() {
      return Optional.ofNullable(technologies);
    }

    @Override
    public Optional<RestrictedField<WorkPeriod>> getWorkPeriod() {
      return Optional.ofNullable(workPeriod);
    }

    @Override
    public Optional<RestrictedField<Money>> getSalary() {
      return Optional.ofNullable(salary);
    }
  }
}
