package org.example.protic.application.negotiation;

import org.example.protic.application.IdentifiedRequest;
import org.example.protic.application.Query;
import org.example.protic.domain.negotiation.NegotiationProjection;

import java.util.UUID;

public class GetNegotiationQuery extends IdentifiedRequest<NegotiationProjection>
    implements Query<NegotiationProjection> {

  public UUID negotiationId;
}
