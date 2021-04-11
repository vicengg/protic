package org.example.protic.application.workexperience;

import org.example.protic.domain.workexperience.FilteredWorkExperience;
import org.example.protic.domain.workexperience.WorkExperience;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface WorkExperienceRepository {

  CompletableFuture<Void> create(WorkExperience workExperienceEntity);

  CompletableFuture<FilteredWorkExperience> findById(UUID id);
}
