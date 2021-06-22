package org.example.protic.acceptance;

import org.example.protic.application.negotiation.GetNegotiationsQuery;
import org.example.protic.application.negotiation.NegotiationRepository;
import org.example.protic.domain.Identifiable;
import org.example.protic.domain.negotiation.Negotiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TestingNegotiationRepository implements NegotiationRepository {

  private static final Map<UUID, Negotiation> NEGOTIATIONS = new HashMap<>();

  @Override
  public CompletableFuture<Void> create(Negotiation negotiation) {
    NEGOTIATIONS.put(negotiation.getId(), negotiation);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> update(Negotiation negotiation) {
    NEGOTIATIONS.put(negotiation.getId(), negotiation);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Negotiation> findById(UUID id) {
    return CompletableFuture.completedFuture(NEGOTIATIONS.get(id));
  }

  @Override
  public CompletableFuture<List<Negotiation>> find(GetNegotiationsQuery query) {
    return CompletableFuture.completedFuture(
        NEGOTIATIONS.values().stream()
            .filter(
                negotiation ->
                    query.scope == GetNegotiationsQuery.Scope.CREATOR
                        ? negotiation.getCreator().equals(query.user)
                        : negotiation.getReceiver().equals(query.user))
            .collect(Collectors.toList()));
  }

  @Override
  public CompletableFuture<List<Negotiation>> findByWorkExperienceId(UUID workExperienceId) {
    return CompletableFuture.completedFuture(
        NEGOTIATIONS.values().stream()
            .filter(
                negotiation ->
                    negotiation.getOfferedWorkExperience().getId().equals(workExperienceId)
                        || negotiation.getDemandedWorkExperience().getId().equals(workExperienceId))
            .collect(Collectors.toList()));
  }

  @Override
  public CompletableFuture<Void> deleteByWorkExperienceId(UUID workExperienceId) {
    NEGOTIATIONS.values().stream()
        .filter(
            negotiation ->
                negotiation.getOfferedWorkExperience().getId().equals(workExperienceId)
                    || negotiation.getDemandedWorkExperience().getId().equals(workExperienceId))
        .map(Identifiable::getId)
        .forEach(NEGOTIATIONS::remove);
    return CompletableFuture.completedFuture(null);
  }
}
