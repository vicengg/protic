package org.example.protic.application.workexperience;

import org.example.protic.domain.workexperience.WorkExperience;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WorkExperienceRepository {

  CompletableFuture<Void> create(WorkExperience workExperienceEntity);

  CompletableFuture<WorkExperience> findById(UUID id);

  CompletableFuture<List<WorkExperience>> find(GetWorkExperiencesQuery query);
}
