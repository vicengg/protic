package org.example.protic.application.workexperience;

import org.example.protic.domain.workexperience.WorkExperienceResponse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WorkExperienceService {

  CompletableFuture<UUID> createWorkExperience(CreateWorkExperienceCommand command);

  CompletableFuture<List<WorkExperienceResponse>> getMyWorkExperiences(
      GetMyWorkExperiencesQuery query);
}
