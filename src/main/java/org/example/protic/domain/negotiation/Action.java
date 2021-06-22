package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;
import org.example.protic.domain.user.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

public class Action implements ValueObject {

  public enum Type {
    MODIFY,
    ACCEPT,
    CANCEL;

    public static Type of(String code) {
      return Arrays.stream(Type.values())
          .filter(c -> c.name().equalsIgnoreCase(code))
          .findFirst()
          .orElseThrow(() -> new ValidationException("Invalid action type code."));
    }
  }

  private final Type type;
  private final User issuer;
  private final Timestamp date;
  private final VisibilityRequest offeredVisibility;
  private final VisibilityRequest demandedVisibility;

  private Action(
      Type type,
      User issuer,
      Timestamp date,
      VisibilityRequest offeredVisibility,
      VisibilityRequest demandedVisibility) {
    this.type =
        Optional.ofNullable(type)
            .orElseThrow(() -> new ValidationException("Action type is required."));
    this.issuer =
        Optional.ofNullable(issuer)
            .orElseThrow(() -> new ValidationException("Action issuer is required."));
    this.date = Optional.ofNullable(date).orElse(Timestamp.from(Instant.now()));
    this.offeredVisibility =
        Optional.ofNullable(offeredVisibility)
            .orElseThrow(() -> new ValidationException("Action offered visibility is required."));
    this.demandedVisibility =
        Optional.ofNullable(demandedVisibility)
            .orElseThrow(() -> new ValidationException("Action demanded visibility is required."));
  }

  public static Action of(
      Type type,
      User issuer,
      Timestamp date,
      VisibilityRequest offeredVisibility,
      VisibilityRequest demandedVisibility) {
    return new Action(type, issuer, date, offeredVisibility, demandedVisibility);
  }

  public Type getType() {
    return type;
  }

  public User getIssuer() {
    return issuer;
  }

  public Timestamp getDate() {
    return date;
  }

  public VisibilityRequest getOfferedVisibility() {
    return offeredVisibility;
  }

  public VisibilityRequest getDemandedVisibility() {
    return demandedVisibility;
  }
}
