package org.example.protic.infrastructure.database.workexperience;

import org.example.protic.domain.user.User;
import org.example.protic.domain.user.UserId;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

public class WorkExperienceAdapterDto implements WorkExperience {

  UUID id;
  Timestamp createdAt;
  User user;
  boolean binding;
  RestrictedField<JobTitle> jobTitle;
  RestrictedField<Company> company;
  RestrictedField<Set<Technology>> technologies;
  RestrictedField<WorkPeriod> workPeriod;
  RestrictedField<Money> salary;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Timestamp getCreatedAt() {
    return createdAt;
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
}
