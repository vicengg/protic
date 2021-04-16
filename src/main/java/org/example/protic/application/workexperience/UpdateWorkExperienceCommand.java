package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.util.Set;
import java.util.UUID;

public class UpdateWorkExperienceCommand implements Command<UUID> {

  public UUID id;
  public UserId userId;
  public boolean binding;
  public WorkExperienceField<JobTitle> jobTitle;
  public WorkExperienceField<Company> company;
  public WorkExperienceField<Set<Technology>> technologies;
  public WorkExperienceField<WorkPeriod> workPeriod;
  public WorkExperienceField<Money> salary;
}
