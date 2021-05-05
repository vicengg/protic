package org.example.protic.application.negotiation;

import org.example.protic.domain.negotiation.Negotiation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NegotiationRepository {

  CompletableFuture<Void> create(Negotiation negotiation);

  CompletableFuture<Void> update(Negotiation negotiation);

  CompletableFuture<Negotiation> findById(UUID id);

  CompletableFuture<List<Negotiation>> find(GetNegotiationsQuery query);

  CompletableFuture<List<Negotiation>> findByWorkExperienceId(UUID workExperienceId);

  CompletableFuture<Void> deleteByWorkExperienceId(UUID workExperienceId);
}
