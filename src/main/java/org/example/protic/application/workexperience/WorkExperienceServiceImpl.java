package org.example.protic.application.workexperience;

import org.example.protic.domain.workexperience.WorkExperience;
import org.example.protic.domain.workexperience.WorkExperienceEntity;
import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WorkExperienceServiceImpl implements WorkExperienceService {

  private final WorkExperienceRepository repository;

  public WorkExperienceServiceImpl(WorkExperienceRepository repository) {
    this.repository = repository;
  }

  @Override
  public CompletableFuture<UUID> createWorkExperience(CreateWorkExperienceCommand command) {
    WorkExperience workExperience =
        WorkExperienceEntity.builder(command.userId, command.binding)
            .withJobTitle(command.jobTitle)
            .withCompany(command.company)
            .withTechnologies(command.technologies)
            .withWorkPeriod(command.workPeriod)
            .build();
    return repository.create(workExperience).thenApply(ignore -> workExperience.getId());
  }

  @Override
  public CompletableFuture<List<WorkExperienceProjection>> getWorkExperiences(
      GetWorkExperiencesQuery query) {
    return repository
        .find(query)
        .thenApply(
            workExperiences -> {
              workExperiences.sort(
                  Comparator.comparing(a -> a.getWorkPeriod().getValue().getStartDate()));
              return workExperiences.stream()
                  .map(WorkExperienceEntity::copy)
                  .map(a -> a.toWorkExperienceResponse(query.userId))
                  .collect(Collectors.toList());
            });
  }
}
