package org.example.protic.infrastructure.database;

import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.workexperience.FilteredWorkExperience;
import org.example.protic.domain.workexperience.WorkExperience;

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
  public CompletableFuture<FilteredWorkExperience> findById(UUID id) {
    return CompletableFuture.supplyAsync(() -> syncAdapter.findById(id));
  }
}
