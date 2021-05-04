package org.example.protic.domain.negotiation;

import org.example.protic.commons.ForbiddenException;
import org.example.protic.commons.UnexpectedException;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.Entity;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.RestrictedField;
import org.example.protic.domain.workexperience.WorkExperience;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class NegotiationEntity extends Entity implements Negotiation {

  private final UUID offeredWorkExperienceId;
  private final UUID demandedWorkExperienceId;
  private final VisibilityRequest offeredData;
  private final VisibilityRequest demandedData;
  private final NegotiationState state;

  private NegotiationEntity(
      User user,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      VisibilityRequest offeredData,
      VisibilityRequest demandedData) {
    this(
        UUID.randomUUID(),
        Timestamp.from(Instant.now()),
        offeredWorkExperience,
        demandedWorkExperience,
        offeredData,
        demandedData,
        NegotiationState.DEMANDED_PENDING);
    Optional.ofNullable(user)
        .orElseThrow(() -> new ValidationException("User is required to create negotiation."));
    checkOfferedWorkExperienceOwnership(user, offeredWorkExperience);
    checkDemandedWorkExperienceOwnership(user, demandedWorkExperience);
  }

  private NegotiationEntity(
      UUID negotiationId,
      Timestamp createdAt,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      VisibilityRequest offeredData,
      VisibilityRequest demandedData,
      NegotiationState negotiationState) {
    super(negotiationId, createdAt);
    this.offeredWorkExperienceId = offeredWorkExperience.getId();
    this.demandedWorkExperienceId = demandedWorkExperience.getId();
    this.offeredData = adjustVisibilityRequest(offeredData, offeredWorkExperience);
    this.demandedData = adjustVisibilityRequest(demandedData, demandedWorkExperience);
    this.state = negotiationState;
  }

  private NegotiationEntity(Negotiation negotiation) {
    super(negotiation.getId(), negotiation.getCreatedAt());
    this.offeredWorkExperienceId = negotiation.getOfferedWorkExperienceId();
    this.demandedWorkExperienceId = negotiation.getDemandedWorkExperienceId();
    this.offeredData = negotiation.getOfferedData();
    this.demandedData = negotiation.getDemandedData();
    this.state = negotiation.getState();
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

  public static NegotiationEntity create(
      User user,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      VisibilityRequest offeredData,
      VisibilityRequest demandedData) {
    return new NegotiationEntity(
        user, offeredWorkExperience, demandedWorkExperience, offeredData, demandedData);
  }

  public static NegotiationEntity copy(Negotiation negotiation) {
    return new NegotiationEntity(negotiation);
  }

  public NegotiationEntity update(
      User user,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      VisibilityRequest offeredData,
      VisibilityRequest demandedData) {
    return modify(
        user,
        offeredWorkExperience,
        demandedWorkExperience,
        () ->
            new NegotiationEntity(
                this.getId(),
                this.getCreatedAt(),
                offeredWorkExperience,
                demandedWorkExperience,
                offeredData,
                demandedData,
                NegotiationState.DEMANDED_PENDING),
        () ->
            new NegotiationEntity(
                this.getId(),
                this.getCreatedAt(),
                offeredWorkExperience,
                demandedWorkExperience,
                offeredData,
                demandedData,
                NegotiationState.OFFERING_PENDING));
  }

  public NegotiationEntity accept(
      User user, WorkExperience offeredWorkExperience, WorkExperience demandedWorkExperience) {
    return modify(
        user,
        offeredWorkExperience,
        demandedWorkExperience,
        () ->
            new NegotiationEntity(
                this.getId(),
                this.getCreatedAt(),
                offeredWorkExperience,
                demandedWorkExperience,
                offeredData,
                demandedData,
                NegotiationState.ACCEPTED));
  }

  public NegotiationEntity cancel(
      User user, WorkExperience offeredWorkExperience, WorkExperience demandedWorkExperience) {
    return modify(
        user,
        offeredWorkExperience,
        demandedWorkExperience,
        () ->
            new NegotiationEntity(
                this.getId(),
                this.getCreatedAt(),
                offeredWorkExperience,
                demandedWorkExperience,
                offeredData,
                demandedData,
                NegotiationState.CANCELLED));
  }

  private NegotiationEntity modify(
      User user,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      Supplier<NegotiationEntity> supplier) {
    return modify(user, offeredWorkExperience, demandedWorkExperience, supplier, supplier);
  }

  private NegotiationEntity modify(
      User user,
      WorkExperience offeredWorkExperience,
      WorkExperience demandedWorkExperience,
      Supplier<NegotiationEntity> offeringPendingSupplier,
      Supplier<NegotiationEntity> demandedPendingSupplier) {
    checkWorkExperienceIntegrity(this.offeredWorkExperienceId, offeredWorkExperience);
    checkWorkExperienceIntegrity(this.demandedWorkExperienceId, demandedWorkExperience);
    switch (this.state) {
      case ACCEPTED:
      case CANCELLED:
        throw new ForbiddenException();
      case OFFERING_PENDING:
        if (!offeredWorkExperience.getUser().getId().equals(user.getId())) {
          throw new ForbiddenException();
        }
        return offeringPendingSupplier.get();
      case DEMANDED_PENDING:
        if (!demandedWorkExperience.getUser().getId().equals(user.getId())) {
          throw new ForbiddenException();
        }
        return demandedPendingSupplier.get();
      default:
        throw new UnexpectedException("Illegal negotiation state.");
    }
  }

  private static VisibilityRequest adjustVisibilityRequest(
      VisibilityRequest visibilityRequest, WorkExperience workExperience) {
    VisibilityRequest.Builder builder = VisibilityRequest.builder();
    builder.withJobTitle(
        calculateVisibility(workExperience.getJobTitle(), visibilityRequest.getJobTitle()));
    builder.withCompany(
        calculateVisibility(workExperience.getCompany(), visibilityRequest.getCompany()));
    builder.withTechnologies(
        calculateVisibility(workExperience.getTechnologies(), visibilityRequest.getTechnologies()));
    builder.withWorkPeriod(
        calculateVisibility(workExperience.getWorkPeriod(), visibilityRequest.getWorkPeriod()));
    builder.withSalary(
        calculateVisibility(workExperience.getSalary(), visibilityRequest.getSalary()));
    return builder.build();
  }

  private static <S> Visibility calculateVisibility(
      RestrictedField<S> restrictedField, Visibility visibility) {
    return restrictedField.isPublic() ? Visibility.ALREADY_VISIBLE : visibility;
  }

  private static void checkWorkExperienceIntegrity(
      UUID workExperienceId, WorkExperience workExperience) {
    if (!workExperienceId.equals(workExperience.getId())) {
      throw new UnexpectedException("Work experience ID doesn't match.");
    }
  }

  private static void checkOfferedWorkExperienceOwnership(
      User user, WorkExperience offeredWorkExperience) {
    if (!user.getId().equals(offeredWorkExperience.getUser().getId())) {
      throw new ForbiddenException();
    }
  }

  private static void checkDemandedWorkExperienceOwnership(
      User user, WorkExperience demandedWorkExperience) {
    if (user.getId().equals(demandedWorkExperience.getUser().getId())) {
      throw new ValidationException("User can't negotiate with own work experiences.");
    }
  }
}
