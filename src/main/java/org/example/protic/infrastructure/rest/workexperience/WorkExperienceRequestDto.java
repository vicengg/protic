package org.example.protic.infrastructure.rest.workexperience;

import java.util.Set;

public class WorkExperienceRequestDto {

  public boolean binding;
  public WorkExperienceFieldDto<String> jobTitle;
  public WorkExperienceFieldDto<String> company;
  public WorkExperienceFieldDto<Set<String>> technologies;
  public WorkExperienceFieldDto<WorkPeriodDto> workPeriod;
  public WorkExperienceFieldDto<MoneyDto> salary;
}
