package org.example.protic.infrastructure.database.negotiation;

import org.example.protic.domain.Identifiable;
import org.example.protic.domain.TimeTraceable;
import org.example.protic.domain.negotiation.Action;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NegotiationAdapterDto implements Negotiation, Identifiable, TimeTraceable {

  UUID id;
  Timestamp createdAt;
  WorkExperience offeredWorkExperience;
  WorkExperience demandedWorkExperience;
  User creator;
  User receiver;
  User nextActor;
  List<Action> actions;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public Timestamp getCreatedAt() {
    return createdAt;
  }

  @Override
  public WorkExperience getOfferedWorkExperience() {
    return offeredWorkExperience;
  }

  @Override
  public WorkExperience getDemandedWorkExperience() {
    return demandedWorkExperience;
  }

  @Override
  public User getCreator() {
    return creator;
  }

  @Override
  public User getReceiver() {
    return receiver;
  }

  @Override
  public Optional<User> getNextActor() {
    return Optional.ofNullable(nextActor);
  }

  @Override
  public List<Action> getActions() {
    return actions;
  }

  @Override
  public boolean isAccepted() {
    return actions.get(actions.size() - 1).getType() == Action.Type.ACCEPT;
  }
}
