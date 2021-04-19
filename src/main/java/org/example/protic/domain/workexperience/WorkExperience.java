package org.example.protic.domain.workexperience;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.UserId;
import org.javamoney.moneta.Money;

import java.util.Set;

public interface WorkExperience extends Identifiable, TimeTraceable {

  UserId getUserId();

  boolean getBinding();

  RestrictedField<JobTitle> getJobTitle();

  RestrictedField<Company> getCompany();

  RestrictedField<Set<Technology>> getTechnologies();

  RestrictedField<WorkPeriod> getWorkPeriod();

  RestrictedField<Money> getSalary();
}
