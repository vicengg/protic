package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.Company;
import org.example.protic.domain.workexperience.JobTitle;
import org.example.protic.domain.workexperience.Technology;
import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.Set;
import java.util.stream.Collectors;

public class WorkExperienceDto {

  public String id;
  public String userId;
  public boolean binding;
  public WorkExperienceFieldDto<String> jobTitle;
  public WorkExperienceFieldDto<String> company;
  public WorkExperienceFieldDto<Set<String>> technologies;
  public WorkExperienceFieldDto<WorkPeriodDto> workPeriod;
  public WorkExperienceFieldDto<MoneyDto> salary;

  public static WorkExperienceDto of(WorkExperienceProjection workExperienceProjection) {
    WorkExperienceDto workExperienceProjectionDto = new WorkExperienceDto();
    workExperienceProjectionDto.id = workExperienceProjection.getId().toString();
    workExperienceProjectionDto.userId =
        workExperienceProjection.getUserId().map(UserId::getValue).orElse(null);
    workExperienceProjectionDto.jobTitle =
        workExperienceProjection
            .getJobTitle()
            .map(field -> WorkExperienceFieldDto.of(field, JobTitle::getName))
            .orElse(null);
    workExperienceProjectionDto.company =
        workExperienceProjection
            .getCompany()
            .map(field -> WorkExperienceFieldDto.of(field, Company::getName))
            .orElse(null);
    workExperienceProjectionDto.technologies =
        workExperienceProjection
            .getTechnologies()
            .map(
                field ->
                    WorkExperienceFieldDto.of(
                        field,
                        technologiesSet ->
                            technologiesSet.stream()
                                .map(Technology::getName)
                                .collect(Collectors.toSet())))
            .orElse(null);
    workExperienceProjectionDto.workPeriod =
        workExperienceProjection
            .getWorkPeriod()
            .map(field -> WorkExperienceFieldDto.of(field, WorkPeriodDto::of))
            .orElse(null);
    workExperienceProjectionDto.salary =
        workExperienceProjection
            .getSalary()
            .map(field -> WorkExperienceFieldDto.of(field, MoneyDto::of))
            .orElse(null);
    return workExperienceProjectionDto;
  }
}