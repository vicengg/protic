package org.example.protic.infrastructure.database.workexperience;

import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.WorkExperienceResponse;
import org.example.protic.domain.workexperience.WorkExperience;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WorkExperienceRepositoryAdapter implements WorkExperienceRepository {

  private final WorkExperienceRepositoryAdapterSync syncAdapter;

  public WorkExperienceRepositoryAdapter(WorkExperienceRepositoryAdapterSync syncAdapter) {
    this.syncAdapter = Objects.requireNonNull(syncAdapter, "Null sync adapter.");
  }

  @Override
  public CompletableFuture<Void> create(WorkExperience workExperience) {
    return CompletableFuture.runAsync(() -> syncAdapter.create(workExperience));
  }

  @Override
  public CompletableFuture<WorkExperience> findById(UUID id) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.findById(id));
  }

  @Override
  public CompletableFuture<List<WorkExperience>> getByUserId(UserId userId) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.getByUserId(userId));
  }
}
