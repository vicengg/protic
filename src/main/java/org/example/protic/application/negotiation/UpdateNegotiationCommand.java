package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;
import org.example.protic.domain.negotiation.Action;

import java.util.UUID;

public class UpdateNegotiationCommand extends IdentifiedRequest<Void> implements Command<Void> {

  public UUID negotiationId;
  public Action action;
}
