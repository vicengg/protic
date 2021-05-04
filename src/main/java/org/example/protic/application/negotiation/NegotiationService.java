package org.example.protic.application.negotiation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NegotiationService {

  CompletableFuture<UUID> createNegotiation(CreateNegotiationCommand command);

  CompletableFuture<Void> updateNegotiation(UpdateNegotiationCommand command);
}
