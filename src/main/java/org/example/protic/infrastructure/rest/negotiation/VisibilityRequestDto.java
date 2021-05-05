package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.domain.negotiation.VisibilityRequest;

public class VisibilityRequestDto {

  public String jobTitle;
  public String company;
  public String technologies;
  public String workPeriod;
  public String salary;

  public static VisibilityRequestDto of(VisibilityRequest visibilityRequest) {
    VisibilityRequestDto visibilityRequestDto = new VisibilityRequestDto();
    visibilityRequestDto.jobTitle = visibilityRequest.getJobTitle().name();
    visibilityRequestDto.company = visibilityRequest.getCompany().name();
    visibilityRequestDto.technologies = visibilityRequest.getTechnologies().name();
    visibilityRequestDto.workPeriod = visibilityRequest.getWorkPeriod().name();
    visibilityRequestDto.salary = visibilityRequest.getSalary().name();
    return visibilityRequestDto;
  }
}
