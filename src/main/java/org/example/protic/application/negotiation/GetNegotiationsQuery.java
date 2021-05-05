package org.example.protic.application.negotiation;

import org.example.protic.application.IdentifiedRequest;
import org.example.protic.application.Query;
import org.example.protic.domain.negotiation.NegotiationProjection;

import java.util.Arrays;
import java.util.List;

public class GetNegotiationsQuery extends IdentifiedRequest<List<NegotiationProjection>>
    implements Query<List<NegotiationProjection>> {

  public enum Scope {
    CREATOR,
    RECEIVER;

    public static GetNegotiationsQuery.Scope of(String code) {
      return Arrays.stream(GetNegotiationsQuery.Scope.values())
          .filter(c -> c.name().equalsIgnoreCase(code))
          .findFirst()
          .orElse(CREATOR);
    }
  }

  public Scope scope;
}
