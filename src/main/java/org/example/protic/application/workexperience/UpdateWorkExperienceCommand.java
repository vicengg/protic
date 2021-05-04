package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.util.Set;
import java.util.UUID;

public class UpdateWorkExperienceCommand extends IdentifiedRequest<Void> implements Command<Void> {

  public UUID id;
  public boolean binding;
  public RestrictedField<JobTitle> jobTitle;
  public RestrictedField<Company> company;
  public RestrictedField<Set<Technology>> technologies;
  public RestrictedField<WorkPeriod> workPeriod;
  public RestrictedField<Money> salary;
}
