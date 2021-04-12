package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.application.workexperience.CreateWorkExperienceCommand;
import org.example.protic.application.workexperience.GetMyWorkExperiencesQuery;
import org.example.protic.application.workexperience.WorkExperienceService;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.rest.IdResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
  public ResponseEntity<IdResponseDto> createWorkExperience(
      @Context SecurityContext securityContext, WorkExperienceRequestDto requestDto) {
    UserId id = getUserId(securityContext);
    return workExperienceService
        .createWorkExperience(mapToCreateWorkExperienceCommand(id, requestDto))
        .thenApply(WorkExperienceResource::toResponse).join();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ResponseEntity<List<WorkExperienceResponseDto>> getMyWorkExperiences(
      @Context SecurityContext securityContext) {
    UserId userId = getUserId(securityContext);
    GetMyWorkExperiencesQuery query = new GetMyWorkExperiencesQuery();
    query.userId = userId;
    return workExperienceService
        .getMyWorkExperiences(query)
        .thenApply(WorkExperienceResource::toResponse).join();
  }

  private UserId getUserId(@Context SecurityContext securityContext) {
    OAuth2AuthenticationToken authenticationToken =
        (OAuth2AuthenticationToken) securityContext.getUserPrincipal();
    OAuth2AuthenticatedPrincipal authenticatedPrincipal = authenticationToken.getPrincipal();
    return UserId.of(Objects.requireNonNull(authenticatedPrincipal.getAttribute("id")).toString());
  }

  private static ResponseEntity<IdResponseDto> toResponse(UUID uuid) {
    return new ResponseEntity<>(IdResponseDto.of(uuid), new HttpHeaders(), HttpStatus.OK);
  }

  private static ResponseEntity<List<WorkExperienceResponseDto>> toResponse(
      List<WorkExperienceResponse> workExperiences) {
    return new ResponseEntity<>(
        workExperiences.stream().map(WorkExperienceResponseDto::of).collect(Collectors.toList()),
        new HttpHeaders(),
        HttpStatus.OK);
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

  private static <R, S extends WorkExperienceFieldDto<R>, T>
      WorkExperienceField<T> toWorkExperienceField(S dto, Function<R, T> mappingFunction) {
    return dto.isPublic
        ? WorkExperienceField.ofPublic((mappingFunction.apply(dto.content)))
        : WorkExperienceField.ofPrivate((mappingFunction.apply(dto.content)));
  }
}
