package org.example.protic.application.workexperience;

import com.spotify.futures.CompletableFutures;
import org.example.protic.application.negotiation.NegotiationRepository;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.WorkExperience;
import org.example.protic.domain.workexperience.WorkExperienceEntity;
import org.example.protic.domain.workexperience.WorkExperienceProjection;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WorkExperienceServiceImpl implements WorkExperienceService {

  private final WorkExperienceRepository workExperienceRepository;
  private final NegotiationRepository negotiationRepository;

  public WorkExperienceServiceImpl(
      WorkExperienceRepository workExperienceRepository,
      NegotiationRepository negotiationRepository) {
    this.workExperienceRepository =
        Objects.requireNonNull(workExperienceRepository, "Null work experience repository.");
    this.negotiationRepository =
        Objects.requireNonNull(negotiationRepository, "Null work negotiation repository.");
  }

  @Override
  public CompletableFuture<UUID> createWorkExperience(CreateWorkExperienceCommand command) {
    WorkExperience workExperience =
        WorkExperienceEntity.builder()
            .withUser(command.user)
            .withBinding(command.binding)
            .withJobTitle(command.jobTitle)
            .withCompany(command.company)
            .withTechnologies(command.technologies)
            .withWorkPeriod(command.workPeriod)
            .withSalary(command.salary)
            .build();
    return workExperienceRepository
        .create(workExperience)
        .thenApply(ignore -> workExperience.getId());
  }

  @Override
  public CompletableFuture<List<WorkExperienceProjection>> getWorkExperiences(
      GetWorkExperiencesQuery query) {
    return workExperienceRepository
        .find(query)
        .thenCompose(
            workExperiences -> {
              workExperiences.sort(moreRecent());
              return CompletableFutures.allAsList(
                  workExperiences.stream()
                      .map(WorkExperienceEntity::copy)
                      .map(
                          workExperienceEntity ->
                              toWorkExperienceProjection(workExperienceEntity, query.user))
                      .collect(Collectors.toList()));
            });
  }

  @Override
  public CompletableFuture<WorkExperienceProjection> getWorkExperience(
      GetWorkExperienceQuery query) {
    return workExperienceRepository
        .findById(query.id)
        .thenApply(WorkExperienceEntity::copy)
        .thenCompose(
            workExperienceEntity -> toWorkExperienceProjection(workExperienceEntity, query.user));
  }

  @Override
  public CompletableFuture<Void> updateWorkExperience(UpdateWorkExperienceCommand command) {
    return workExperienceRepository
        .findById(command.id)
        .thenApply(WorkExperienceEntity::copy)
        .thenApply(workExperienceEntity -> workExperienceEntity.update(command.user))
        .thenApply(
            updater ->
                updater
                    .withUser(command.user)
                    .withBinding(command.binding)
                    .withJobTitle(command.jobTitle)
                    .withCompany(command.company)
                    .withTechnologies(command.technologies)
                    .withWorkPeriod(command.workPeriod)
                    .withSalary(command.salary)
                    .build())
        .thenCompose(workExperienceRepository::update);
  }

  @Override
  public CompletableFuture<Void> deleteWorkExperience(DeleteWorkExperienceCommand command) {
    return workExperienceRepository
        .findById(command.id)
        .thenApply(WorkExperienceEntity::copy)
        .thenApply(workExperienceEntity -> workExperienceEntity.checkForDelete(command.user))
        .thenCompose(workExperienceRepository::delete);
  }

  private CompletableFuture<WorkExperienceProjection> toWorkExperienceProjection(
      WorkExperienceEntity workExperience, User user) {
    return negotiationRepository
        .findByWorkExperienceId(workExperience.getId())
        .thenApply(negotiations -> workExperience.toWorkExperienceProjection(user, negotiations));
  }

  private static Comparator<WorkExperience> moreRecent() {
    return (o1, o2) ->
        o2.getWorkPeriod()
            .getValue()
            .getStartDate()
            .compareTo(o1.getWorkPeriod().getValue().getStartDate());
  }
}
