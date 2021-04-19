package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.domain.UserId;

import java.util.UUID;

public class CancelNegotiationCommand implements Command<Void> {

  public UserId userId;
  public UUID negotiationId;
}
