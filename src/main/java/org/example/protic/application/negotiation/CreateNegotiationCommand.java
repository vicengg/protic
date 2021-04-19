package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;
import org.example.protic.domain.negotiation.VisibilityRequest;

import java.util.UUID;

public class CreateNegotiationCommand implements Command<UUID> {

  public UserId userId;
  public UUID offeredWorkExperienceId;
  public UUID demandedWorkExperienceId;
  public VisibilityRequest offeredData;
  public VisibilityRequest demandedData;
}
