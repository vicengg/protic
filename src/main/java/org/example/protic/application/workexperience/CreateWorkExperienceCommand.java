package org.example.protic.application.workexperience;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;

import java.util.Set;
import java.util.UUID;

public class CreateWorkExperienceCommand implements Command<UUID> {

  public UserId userId;
  public boolean binding;
  public WorkExperienceField<JobTitle> jobTitle;
  public WorkExperienceField<Company> company;
  public WorkExperienceField<Set<Technology>> technologies;
  public WorkExperienceField<WorkPeriod> workPeriod;
}
