package org.example.protic.domain.negotiation;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;

import java.util.UUID;

public interface Negotiation extends Identifiable, TimeTraceable {

  UUID getOfferedWorkExperienceId();

  UUID getDemandedWorkExperienceId();

  VisibilityRequest getOfferedData();

  VisibilityRequest getDemandedData();

  NegotiationState getState();
}
