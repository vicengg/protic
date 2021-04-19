package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;

import java.util.Arrays;

public enum NegotiationState {
  OFFERING_PENDING,
  DEMANDED_PENDING,
  ACCEPTED,
  CANCELLED;

  public static NegotiationState of(String code) {
    return Arrays.stream(NegotiationState.values())
        .filter(c -> c.name().equalsIgnoreCase(code))
        .findFirst()
        .orElseThrow(() -> new ValidationException("Invalid negotiation state code."));
  }
}
