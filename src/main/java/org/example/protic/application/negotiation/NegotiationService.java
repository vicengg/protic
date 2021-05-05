package org.example.protic.application.negotiation;

import org.example.protic.domain.negotiation.NegotiationProjection;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NegotiationService {

  CompletableFuture<UUID> createNegotiation(CreateNegotiationCommand command);

  CompletableFuture<Void> updateNegotiation(UpdateNegotiationCommand command);

  CompletableFuture<NegotiationProjection> getNegotiation(GetNegotiationQuery command);

  CompletableFuture<List<NegotiationProjection>> getNegotiations(GetNegotiationsQuery query);
}
