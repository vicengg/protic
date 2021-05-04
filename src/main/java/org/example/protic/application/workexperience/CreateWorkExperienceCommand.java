package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;
import org.example.protic.domain.user.User;
import org.example.protic.domain.user.UserId;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CreateWorkExperienceCommand extends IdentifiedRequest<UUID> implements Command<UUID> {

  public boolean binding;
  public RestrictedField<JobTitle> jobTitle;
  public RestrictedField<Company> company;
  public RestrictedField<Set<Technology>> technologies;
  public RestrictedField<WorkPeriod> workPeriod;
  public RestrictedField<Money> salary;
}
