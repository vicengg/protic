package org.example.protic.application.negotiation;

import com.spotify.futures.CompletableFutures;
import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.NegotiationEntity;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NegotiationServiceImpl implements NegotiationService {

  private final WorkExperienceRepository workExperienceRepository;
  private final NegotiationRepository negotiationRepository;

  public NegotiationServiceImpl(
      WorkExperienceRepository workExperienceRepository,
      NegotiationRepository negotiationRepository) {
    this.workExperienceRepository =
        Objects.requireNonNull(workExperienceRepository, "Null work experience repository.");
    this.negotiationRepository =
        Objects.requireNonNull(negotiationRepository, "Null negotiation repository.");
  }

  @Override
  public CompletableFuture<UUID> createNegotiation(CreateNegotiationCommand command) {
    return CompletableFutures.combine(
            workExperienceRepository.findById(command.offeredWorkExperienceId),
            workExperienceRepository.findById(command.demandedWorkExperienceId),
            (offeredWorkExperience, demandedWorkExperience) ->
                NegotiationEntity.create(
                    command.user, offeredWorkExperience, demandedWorkExperience))
        .thenCompose(this::createNegotiationAndReturnId)
        .toCompletableFuture();
  }

  @Override
  public CompletableFuture<Void> updateNegotiation(UpdateNegotiationCommand command) {
    return negotiationRepository
        .find(command.negotiationId)
        .thenApply(NegotiationEntity::copy)
        .thenApply(negotiation -> negotiation.addAction(command.action))
        .thenCompose(negotiationRepository::update);
  }

  private CompletableFuture<UUID> createNegotiationAndReturnId(Negotiation negotiation) {
    return negotiationRepository.create(negotiation).thenApply(ignore -> negotiation.getId());
  }
}
