package org.example.protic.application.negotiation;

import org.example.protic.application.Command;
import org.example.protic.application.IdentifiedRequest;

import java.util.UUID;

public class AcceptNegotiationCommand extends IdentifiedRequest<Void> implements Command<Void> {

  public UUID negotiationId;
}
