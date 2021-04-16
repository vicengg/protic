package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.Company;
import org.example.protic.domain.workexperience.JobTitle;
import org.example.protic.domain.workexperience.Technology;
import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.Set;
import java.util.stream.Collectors;

public class WorkExperienceProjectionDto {

  public String id;
  public String userId;
  public String jobTile;
  public String company;
  public Set<String> technologies;
  public WorkPeriodDto workPeriod;
  public MoneyDto salary;

  public static WorkExperienceProjectionDto of(WorkExperienceProjection workExperienceProjection) {
    WorkExperienceProjectionDto workExperienceProjectionDto = new WorkExperienceProjectionDto();
    workExperienceProjectionDto.id = workExperienceProjection.getId().toString();
    workExperienceProjectionDto.userId =
        workExperienceProjection.getUserId().map(UserId::getValue).orElse(null);
    workExperienceProjectionDto.jobTile =
        workExperienceProjection.getJobTitle().map(JobTitle::getName).orElse(null);
    workExperienceProjectionDto.company =
        workExperienceProjection.getCompany().map(Company::getName).orElse(null);
    workExperienceProjectionDto.technologies =
        workExperienceProjection
            .getTechnologies()
            .map(
                technologiesSet ->
                    technologiesSet.stream().map(Technology::getName).collect(Collectors.toSet()))
            .orElse(null);
    workExperienceProjectionDto.workPeriod =
        workExperienceProjection.getWorkPeriod().map(WorkPeriodDto::of).orElse(null);
    workExperienceProjectionDto.salary =
            workExperienceProjection.getSalary().map(MoneyDto::of).orElse(null);
    return workExperienceProjectionDto;
  }
}
