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
        WorkExperienceEntity.builder()
            .withUserId(command.userId)
            .withBinding(command.binding)
            .withJobTitle(command.jobTitle)
            .withCompany(command.company)
            .withTechnologies(command.technologies)
            .withWorkPeriod(command.workPeriod)
            .withSalary(command.salary)
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
              workExperiences.sort(moreRecent());
              return workExperiences.stream()
                  .map(WorkExperienceEntity::copy)
                  .map(a -> a.toWorkExperienceResponse(query.userId))
                  .collect(Collectors.toList());
            });
  }

  @Override
  public CompletableFuture<Void> updateWorkExperience(UpdateWorkExperienceCommand command) {
    return repository
        .findById(command.id)
        .thenApply(WorkExperienceEntity::copy)
        .thenApply(workExperienceEntity -> workExperienceEntity.update(command.userId))
        .thenApply(
            updater ->
                updater
                    .withUserId(command.userId)
                    .withBinding(command.binding)
                    .withJobTitle(command.jobTitle)
                    .withCompany(command.company)
                    .withTechnologies(command.technologies)
                    .withWorkPeriod(command.workPeriod)
                    .withSalary(command.salary)
                    .build())
        .thenCompose(repository::updateWorkExperience);
  }

  private static Comparator<WorkExperience> moreRecent() {
    return (o1, o2) ->
        o2.getWorkPeriod()
            .getValue()
            .getStartDate()
            .compareTo(o1.getWorkPeriod().getValue().getStartDate());
  }
}
