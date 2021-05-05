package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.domain.negotiation.NegotiationProjection;
import org.example.protic.infrastructure.rest.RestDto;
import org.example.protic.infrastructure.rest.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class NegotiationDto implements RestDto {

  public String id;
  public String offeredWorkExperienceId;
  public String demandedWorkExperienceId;
  public UserDto creator;
  public UserDto receiver;
  public UserDto nextActor;
  public List<ActionDto> actions;

  public static NegotiationDto of(NegotiationProjection negotiation) {
    NegotiationDto negotiationDto = new NegotiationDto();
    negotiationDto.id = negotiation.getId().toString();
    negotiationDto.offeredWorkExperienceId = negotiation.getOfferedWorkExperienceId().toString();
    negotiationDto.demandedWorkExperienceId = negotiation.getDemandedWorkExperienceId().toString();
    negotiationDto.creator = UserDto.of(negotiation.getCreator());
    negotiationDto.receiver = UserDto.of(negotiation.getReceiver());
    negotiationDto.nextActor = negotiation.getNextActor().map(UserDto::of).orElse(null);
    negotiationDto.actions =
        negotiation.getActions().stream().map(ActionDto::of).collect(Collectors.toList());
    return negotiationDto;
  }
}
