package org.example.protic.acceptance;

import org.example.protic.application.workexperience.GetWorkExperiencesQuery;
import org.example.protic.application.workexperience.WorkExperienceRepository;
import org.example.protic.domain.workexperience.WorkExperience;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TestingWorkExperienceRepository implements WorkExperienceRepository {

  private static final Map<UUID, WorkExperience> WORK_EXPERIENCES = new HashMap<>();

  @Override
  public CompletableFuture<Void> create(WorkExperience workExperience) {
    WORK_EXPERIENCES.put(workExperience.getId(), workExperience);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<WorkExperience> findById(UUID id) {
    return CompletableFuture.completedFuture(WORK_EXPERIENCES.get(id));
  }

  @Override
  public CompletableFuture<List<WorkExperience>> find(GetWorkExperiencesQuery query) {
    return CompletableFuture.completedFuture(
        WORK_EXPERIENCES.values().stream()
            .filter(
                we ->
                    query.scope != GetWorkExperiencesQuery.Scope.OWN
                        || we.getUser().equals(query.user))
            .filter(
                we ->
                    query.scope != GetWorkExperiencesQuery.Scope.FOREIGN
                        || !we.getUser().equals(query.user))
            .filter(
                we ->
                    Optional.ofNullable(query.jobTitle)
                        .map(value -> we.getJobTitle().getValue().equals(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.company)
                        .map(value -> we.getCompany().getValue().equals(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.technologies)
                        .map(value -> we.getTechnologies().getValue().containsAll(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.startDate)
                        .map(value -> we.getWorkPeriod().getValue().getStartDate().isAfter(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.endDate)
                        .map(value -> we.getWorkPeriod().getValue().getStartDate().isBefore(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.minSalary)
                        .map(value -> we.getSalary().getValue().isGreaterThanOrEqualTo(value))
                        .orElse(true))
            .filter(
                we ->
                    Optional.ofNullable(query.maxSalary)
                        .map(value -> we.getSalary().getValue().isLessThanOrEqualTo(value))
                        .orElse(true))
            .collect(Collectors.toList()));
  }

  @Override
  public CompletableFuture<Void> update(WorkExperience workExperience) {
    WORK_EXPERIENCES.put(workExperience.getId(), workExperience);
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> delete(UUID id) {
    WORK_EXPERIENCES.remove(id);
    return CompletableFuture.completedFuture(null);
  }
}
