package org.example.protic.domain.negotiation;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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
    this.nextActor = negotiation.getNextActor().orElse(null);
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
    switch (action.getType()) {
      case MODIFY:
        checkActionIssuerIsNextActor(action);
        addModifyAction(action);
        break;
      case ACCEPT:
        checkActionIssuerIsNextActor(action);
        addAcceptAction(action);
        break;
      case CANCEL:
        addCancelAction(action);
        break;
      default:
        break;
    }
    return this;
  }

  private void addModifyAction(Action action) {
    this.actions.add(action);
    toggleNextActor();
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

  private void addCancelAction(Action action) {
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

  @Override
  public boolean isAccepted() {
    return actions.get(actions.size() - 1).getType() == Action.Type.ACCEPT;
  }

  public NegotiationProjection toNegotiationProjection(User user) {
    NegotiationProjectionImpl negotiationProjection = new NegotiationProjectionImpl();
    negotiationProjection.id = this.getId();
    negotiationProjection.createdAt = this.getCreatedAt();
    negotiationProjection.offeredWorkExperienceId = this.offeredWorkExperience.getId();
    negotiationProjection.demandedWorkExperienceId = this.demandedWorkExperience.getId();
    negotiationProjection.creator =
        controlVisibility(user, this.creator, this.offeredWorkExperience);
    negotiationProjection.receiver =
        controlVisibility(user, this.receiver, this.demandedWorkExperience);
    if (Objects.nonNull(this.nextActor)) {
      if (this.nextActor.getId().equals(this.creator.getId())) {
        negotiationProjection.nextActor = negotiationProjection.creator;
      } else {
        negotiationProjection.nextActor = negotiationProjection.receiver;
      }
    }
    negotiationProjection.actions =
        this.actions.stream()
            .map(
                action ->
                    controlVisibility(
                        action,
                        this.creator,
                        negotiationProjection.creator,
                        negotiationProjection.receiver))
            .collect(Collectors.toList());
    return negotiationProjection;
  }

  private Action controlVisibility(
      Action action, User creator, User maskedCreator, User maskedReceiver) {
    User issuer;
    if (action.getIssuer().getId().equals(creator.getId())) {
      issuer = maskedCreator;
    } else {
      issuer = maskedReceiver;
    }
    return Action.of(
        action.getType(),
        issuer,
        action.getDate(),
        action.getOfferedVisibility(),
        action.getDemandedVisibility());
  }

  private static User controlVisibility(
      User user, User userToControl, WorkExperience workExperience) {
    if (user.getId().equals(userToControl.getId())) {
      return userToControl;
    }
    if (workExperience.getBinding()) {
      return userToControl;
    }
    return User.anonymous();
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

  private static final class NegotiationProjectionImpl implements NegotiationProjection {

    private UUID id;
    private Timestamp createdAt;
    private UUID offeredWorkExperienceId;
    private UUID demandedWorkExperienceId;
    private User creator;
    private User receiver;
    private List<Action> actions;
    private User nextActor;

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
  }
}
