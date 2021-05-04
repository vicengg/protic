package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NegotiationEntity extends Entity implements Negotiation {

  private final WorkExperience offeredWorkExperience;
  private final WorkExperience demandedWorkExperience;
  private final User creator;
  private final User receiver;
  private final List<Action> actions;
  private User nextActor;

  private NegotiationEntity(
      User creator, WorkExperience offeredWorkExperience, WorkExperience demandedWorkExperience) {
    super(UUID.randomUUID(), Timestamp.from(Instant.now()));
    this.offeredWorkExperience =
        Optional.ofNullable(offeredWorkExperience)
            .orElseThrow(
                () ->
                    new ValidationException(
                        "Offered work experience needed to create negotiation."));
    this.demandedWorkExperience =
        Optional.ofNullable(demandedWorkExperience)
            .orElseThrow(
                () ->
                    new ValidationException(
                        "Demanded work experience needed to create negotiation."));
    checkCreatorIsOwnerOfTheOffer(offeredWorkExperience, creator);
    this.creator =
        Optional.ofNullable(creator)
            .orElseThrow(() -> new ValidationException("Creator needed to create negotiation."));
    this.receiver =
        Optional.ofNullable(demandedWorkExperience.getUser())
            .orElseThrow(() -> new ValidationException("Receiver needed to create negotiation."));
    this.actions = new ArrayList<>();
    this.nextActor = creator;
  }

  private NegotiationEntity(Negotiation negotiation) {
    super(negotiation.getId(), negotiation.getCreatedAt());
    this.offeredWorkExperience = negotiation.getOfferedWorkExperience();
    this.demandedWorkExperience = negotiation.getDemandedWorkExperience();
    this.creator = negotiation.getCreator();
    this.receiver = negotiation.getReceiver();
    this.actions = negotiation.getActions();
    this.nextActor = negotiation.getCreator();
  }

  public static NegotiationEntity create(
      User creator, WorkExperience offeredWorkExperience, WorkExperience demandedWorkExperience) {
    return new NegotiationEntity(creator, offeredWorkExperience, demandedWorkExperience);
  }

  public static NegotiationEntity copy(Negotiation negotiation) {
    return new NegotiationEntity(negotiation);
  }

  public NegotiationEntity addAction(Action action) {
    checkNegotiationIsOpen();
    checkActionIssuerIsNextActor(action);

    switch (action.getType()) {
      case MODIFY:
        addModifyAction(action);
        break;
      case ACCEPT:
        addAcceptAction(action);
        break;
      case CANCEL:
        break;
      default:
        break;
    }
    return this;
  }

  private void addModifyAction(Action action) {
    if (this.offeredWorkExperience
            .getId()
            .equals(action.getOfferedVisibility().getWorkExperienceId())
        && this.demandedWorkExperience
            .getId()
            .equals(action.getDemandedVisibility().getWorkExperienceId())) {
      this.actions.add(action);
      toggleNextActor();
    } else {
      throw new ValidationException("Illegal action.");
    }
  }

  private void addAcceptAction(Action action) {
    Action lastAction = this.actions.get(this.actions.size() - 1);
    if (lastAction.getOfferedVisibility().equals(action.getOfferedVisibility())
        && lastAction.getDemandedVisibility().equals(action.getDemandedVisibility())) {
      this.actions.add(action);
      this.nextActor = null;
    } else {
      throw new ValidationException("Illegal action.");
    }
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
  public List<Action> getActions() {
    return actions;
  }

  @Override
  public Optional<User> getNextActor() {
    return Optional.ofNullable(nextActor);
  }

  private void toggleNextActor() {
    if (this.creator.getId().equals(this.nextActor.getId())) {
      this.nextActor = this.receiver;
    } else {
      this.nextActor = this.creator;
    }
  }

  private void checkNegotiationIsOpen() {
    if (getNextActor().isEmpty()) {
      throw new ValidationException("The negotiation does not admit more actions.");
    }
  }

  private static void checkCreatorIsOwnerOfTheOffer(
      WorkExperience offeredWorkExperience, User creator) {
    if (!offeredWorkExperience.getUser().getId().equals(creator.getId())) {
      throw new ValidationException(
          "The negotiation creator must be the offered work experience owner.");
    }
  }

  private void checkActionIssuerIsNextActor(Action action) {
    if (!this.getNextActor()
        .map(User::getId)
        .orElseThrow(IllegalStateException::new)
        .equals(action.getIssuer().getId())) {
      throw new ValidationException("Action issuer is not the next negotiation actor.");
    }
  }
}
