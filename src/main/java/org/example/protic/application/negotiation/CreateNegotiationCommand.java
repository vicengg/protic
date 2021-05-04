package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;
import org.example.protic.domain.negotiation.VisibilityRequest;

import java.util.UUID;

public class CreateNegotiationCommand extends IdentifiedRequest<UUID> implements Command<UUID> {

  public UUID offeredWorkExperienceId;
  public UUID demandedWorkExperienceId;
}
