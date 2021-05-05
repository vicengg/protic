package org.example.protic.application.negotiation;

import com.spotify.futures.CompletableFutures;
import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.negotiation.Negotiation;
import org.example.protic.domain.negotiation.NegotiationEntity;
import org.example.protic.domain.negotiation.NegotiationProjection;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
        .findById(command.negotiationId)
        .thenApply(NegotiationEntity::copy)
        .thenApply(negotiation -> negotiation.addAction(command.action))
        .thenCompose(negotiationRepository::update);
  }

  @Override
  public CompletableFuture<NegotiationProjection> getNegotiation(GetNegotiationQuery command) {
    return negotiationRepository
        .findById(command.negotiationId)
        .thenApply(NegotiationEntity::copy)
        .thenApply(negotiation -> negotiation.toNegotiationProjection(command.user));
  }

  @Override
  public CompletableFuture<List<NegotiationProjection>> getNegotiations(
      GetNegotiationsQuery query) {
    return negotiationRepository
        .find(query)
        .thenApply(
            negotiations -> {
              negotiations.sort(moreRecent());
              return negotiations.stream()
                  .map(NegotiationEntity::copy)
                  .map(
                      workExperienceEntity ->
                          workExperienceEntity.toNegotiationProjection(query.user))
                  .collect(Collectors.toList());
            });
  }

  private CompletableFuture<UUID> createNegotiationAndReturnId(Negotiation negotiation) {
    return negotiationRepository.create(negotiation).thenApply(ignore -> negotiation.getId());
  }

  private static Comparator<Negotiation> moreRecent() {
    return (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt());
  }
}
