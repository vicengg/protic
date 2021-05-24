package org.example.protic.infrastructure.rest.negotiation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.example.protic.domain.negotiation.NegotiationProjection;
import org.example.protic.infrastructure.rest.RestDto;
import org.example.protic.infrastructure.rest.user.UserDto;

import java.time.LocalDateTime;
import java.util.Date;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  public Date createdAt;

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
    negotiationDto.createdAt = negotiation.getCreatedAt();
    return negotiationDto;
  }
}
