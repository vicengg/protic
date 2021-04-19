package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;
import org.example.protic.domain.negotiation.VisibilityRequest;

import java.util.UUID;

public class UpdateNegotiationCommand implements Command<Void> {

  public UserId userId;
  public UUID negotiationId;
  public VisibilityRequest offeredData;
  public VisibilityRequest demandedData;
}
