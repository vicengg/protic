package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.infrastructure.rest.RestDto;

public class NegotiationDto implements RestDto {
  public String offeredWorkExperienceId;
  public String demandedWorkExperienceId;
  public VisibilityRequestDto offeredData;
  public VisibilityRequestDto demandedData;
}
