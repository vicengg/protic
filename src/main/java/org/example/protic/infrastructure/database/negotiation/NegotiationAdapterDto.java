package org.example.protic.infrastructure.database.negotiation;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.NegotiationState;
import org.example.protic.domain.negotiation.VisibilityRequest;

import java.sql.Timestamp;
import java.util.UUID;

public class NegotiationAdapterDto implements Negotiation, Identifiable, TimeTraceable {

  UUID id;
  Timestamp createdAt;
  UUID offeredWorkExperienceId;
  UUID demandedWorkExperienceId;
  VisibilityRequest offeredData;
  VisibilityRequest demandedData;
  NegotiationState state;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  @Override
  public UUID getOfferedWorkExperienceId() {
    return offeredWorkExperienceId;
  }

  @Override
  public UUID getDemandedWorkExperienceId() {
    return demandedWorkExperienceId;
  }

  @Override
  public VisibilityRequest getOfferedData() {
    return offeredData;
  }

  @Override
  public VisibilityRequest getDemandedData() {
    return demandedData;
  }

  @Override
  public NegotiationState getState() {
    return state;
  }
}
