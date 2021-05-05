package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.domain.negotiation.Action;
import org.example.protic.infrastructure.rest.user.UserDto;

import java.sql.Timestamp;

public class ActionDto {

  public String type;
  public Timestamp date;
  public VisibilityRequestDto offeredData;
  public VisibilityRequestDto demandedData;
  public UserDto issuer;

  public static ActionDto of(Action action) {
    ActionDto actionDto = new ActionDto();
    actionDto.type = action.getType().name();
    actionDto.date = action.getDate();
    actionDto.issuer = UserDto.of(action.getIssuer());
    actionDto.offeredData = VisibilityRequestDto.of(action.getOfferedVisibility());
    actionDto.demandedData = VisibilityRequestDto.of(action.getDemandedVisibility());
    return actionDto;
  }
}
