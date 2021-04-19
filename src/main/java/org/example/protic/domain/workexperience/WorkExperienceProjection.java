package org.example.protic.domain.workexperience;

import org.example.protic.domain.UserId;
import org.javamoney.moneta.Money;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WorkExperienceProjection {

  UUID getId();

  Optional<UserId> getUserId();

  Optional<RestrictedField<JobTitle>> getJobTitle();

  Optional<RestrictedField<Company>> getCompany();

  Optional<RestrictedField<Set<Technology>>> getTechnologies();

  Optional<RestrictedField<WorkPeriod>> getWorkPeriod();

  Optional<RestrictedField<Money>> getSalary();
}
