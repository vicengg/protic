package org.example.protic.domain.workexperience;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.user.User;
import org.javamoney.moneta.Money;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface WorkExperienceProjection extends Identifiable, TimeTraceable {

  Optional<User> getUser();

  Optional<RestrictedField<JobTitle>> getJobTitle();

  Optional<RestrictedField<Company>> getCompany();

  Optional<RestrictedField<Set<Technology>>> getTechnologies();

  Optional<RestrictedField<WorkPeriod>> getWorkPeriod();

  Optional<RestrictedField<Money>> getSalary();
}
