package org.example.protic.application.workexperience;

import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WorkExperienceService {

  CompletableFuture<UUID> createWorkExperience(CreateWorkExperienceCommand command);

  CompletableFuture<List<WorkExperienceProjection>> getWorkExperiences(
      GetWorkExperiencesQuery query);

  CompletableFuture<Void> updateWorkExperience(
      UpdateWorkExperienceCommand command);
}
