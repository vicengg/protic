package org.example.protic.infrastructure.database.negotiation;

import org.example.protic.application.negotiation.GetNegotiationsQuery;
import org.example.protic.application.negotiation.NegotiationRepository;
import org.example.protic.domain.negotiation.Negotiation;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NegotiationRepositoryAdapter implements NegotiationRepository {

  private final NegotiationRepositoryAdapterSync syncAdapter;

  public NegotiationRepositoryAdapter(NegotiationRepositoryAdapterSync syncAdapter) {
    this.syncAdapter = Objects.requireNonNull(syncAdapter, "Null sync adapter.");
  }

  @Override
  public CompletableFuture<Void> create(Negotiation negotiation) {
    return CompletableFuture.runAsync(() -> syncAdapter.create(negotiation));
  }

  @Override
  public CompletableFuture<Void> update(Negotiation negotiation) {
    return CompletableFuture.runAsync(() -> syncAdapter.update(negotiation));
  }

  @Override
  public CompletableFuture<Negotiation> findById(UUID id) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.findById(id));
  }

  @Override
  public CompletableFuture<List<Negotiation>> find(GetNegotiationsQuery query) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.find(query));
  }

  @Override
  public CompletableFuture<List<Negotiation>> findByWorkExperienceId(UUID workExperienceId) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.findByWorkExperienceId(workExperienceId));
  }
}
