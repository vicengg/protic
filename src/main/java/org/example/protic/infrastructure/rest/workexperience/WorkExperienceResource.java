package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.application.workexperience.CreateWorkExperienceCommand;
import org.example.protic.application.workexperience.GetWorkExperiencesQuery;
import org.example.protic.application.workexperience.WorkExperienceService;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.rest.ExceptionMapper;
import org.example.protic.infrastructure.rest.IdResponseDto;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/work-experience")
public class WorkExperienceResource {

  private final WorkExperienceService workExperienceService;

  @Inject
  public WorkExperienceResource(WorkExperienceService workExperienceService) {
    this.workExperienceService = workExperienceService;
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public void createWorkExperience(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      WorkExperienceRequestDto requestDto) {
    UserId id = getUserId(securityContext);
    workExperienceService
        .createWorkExperience(mapToCreateWorkExperienceCommand(id, requestDto))
        .thenApply(WorkExperienceResource::toResponse)
        .exceptionally(ExceptionMapper::map)
        .thenAccept(asyncResponse::resume);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public void getWorkExperiences(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @DefaultValue("all") @QueryParam("scope") String scope,
      @QueryParam("jobTitle") String jobTitle,
      @QueryParam("company") String company) {
    GetWorkExperiencesQuery query =
        mapToGetWorkExperiencesQuery(securityContext, scope, jobTitle, company);
    workExperienceService
        .getWorkExperiences(query)
        .thenApply(WorkExperienceResource::toResponse)
        .exceptionally(ExceptionMapper::map)
        .thenAccept(asyncResponse::resume);
  }

  private static UserId getUserId(@Context SecurityContext securityContext) {
    OAuth2AuthenticationToken authenticationToken =
        (OAuth2AuthenticationToken) securityContext.getUserPrincipal();
    OAuth2AuthenticatedPrincipal authenticatedPrincipal = authenticationToken.getPrincipal();
    return UserId.of(Objects.requireNonNull(authenticatedPrincipal.getAttribute("id")).toString());
  }

  private static Response toResponse(UUID uuid) {
    return Response.ok(IdResponseDto.of(uuid)).build();
  }

  private static Response toResponse(List<WorkExperienceProjection> workExperiences) {
    return Response.ok(
            workExperiences.stream()
                .map(WorkExperienceProjectionDto::of)
                .collect(Collectors.toList()))
        .build();
  }

  private static CreateWorkExperienceCommand mapToCreateWorkExperienceCommand(
      UserId userId, WorkExperienceRequestDto dto) {
    CreateWorkExperienceCommand command = new CreateWorkExperienceCommand();
    command.userId = userId;
    command.binding = dto.binding;
    command.jobTitle = toWorkExperienceField(dto.jobTitle, JobTitle::of);
    command.company = toWorkExperienceField(dto.company, Company::of);
    command.technologies =
        toWorkExperienceField(
            dto.technologies,
            technologySet ->
                technologySet.stream().map(Technology::of).collect(Collectors.toSet()));
    command.workPeriod =
        toWorkExperienceField(
            dto.workPeriod,
            workPeriodDto ->
                Optional.ofNullable(workPeriodDto.endDate)
                    .map(date -> WorkPeriod.from(workPeriodDto.startDate).to(date))
                    .orElse(WorkPeriod.from(workPeriodDto.startDate).toPresent()));
    return command;
  }

  private static GetWorkExperiencesQuery mapToGetWorkExperiencesQuery(
      SecurityContext securityContext, String scope, String jobTitle, String company) {
    UserId userId = getUserId(securityContext);
    GetWorkExperiencesQuery query = new GetWorkExperiencesQuery();
    query.userId = userId;
    query.scope = GetWorkExperiencesQuery.Scope.of(scope);
    query.jobTitle = Optional.ofNullable(jobTitle).map(JobTitle::of).orElse(null);
    query.company = Optional.ofNullable(company).map(Company::of).orElse(null);
    return query;
  }

  private static <R, S extends WorkExperienceFieldDto<R>, T>
      WorkExperienceField<T> toWorkExperienceField(S dto, Function<R, T> mappingFunction) {
    return dto.isPublic
        ? WorkExperienceField.ofPublic((mappingFunction.apply(dto.content)))
        : WorkExperienceField.ofPrivate((mappingFunction.apply(dto.content)));
  }
}
