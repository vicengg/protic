package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.Company;
import org.example.protic.domain.workexperience.JobTitle;
import org.example.protic.domain.workexperience.Technology;
import org.example.protic.domain.workexperience.WorkExperienceResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class WorkExperienceResponseDto {

  public String userId;
  public String jobTile;
  public String company;
  public Set<String> technologies;
  public WorkPeriodDto workPeriod;

  public static WorkExperienceResponseDto of(WorkExperienceResponse workExperienceResponse) {
    WorkExperienceResponseDto workExperienceResponseDto = new WorkExperienceResponseDto();
    workExperienceResponseDto.userId =
        workExperienceResponse.getUserId().map(UserId::getValue).orElse(null);
    workExperienceResponseDto.jobTile =
        workExperienceResponse.getJobTitle().map(JobTitle::getName).orElse(null);
    workExperienceResponseDto.company =
        workExperienceResponse.getCompany().map(Company::getName).orElse(null);
    workExperienceResponseDto.technologies =
        workExperienceResponse
            .getTechnologies()
            .map(
                technologiesSet ->
                    technologiesSet.stream().map(Technology::getName).collect(Collectors.toSet()))
            .orElse(null);
    workExperienceResponseDto.workPeriod =
        workExperienceResponse.getWorkPeriod().map(WorkPeriodDto::of).orElse(null);
    return workExperienceResponseDto;
  }
}
