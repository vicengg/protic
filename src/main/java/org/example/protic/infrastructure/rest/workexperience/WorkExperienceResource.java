package org.example.protic.infrastructure.rest.workexperience;

import org.example.protic.application.workexperience.*;
import org.example.protic.commons.CurrencyContext;
import org.example.protic.domain.UserId;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.rest.ExceptionMapper;
import org.example.protic.infrastructure.rest.IdResponseDto;
import org.javamoney.moneta.Money;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/work-experience")
public class WorkExperienceResource {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
      WorkExperienceDto requestDto) {
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
      @QueryParam("company") String company,
      @QueryParam("technologies") Set<String> technologies,
      @QueryParam("startDate") String startDate,
      @QueryParam("endDate") String endDate,
      @QueryParam("minSalary") BigDecimal minSalary,
      @QueryParam("maxSalary") BigDecimal maxSalary) {
    GetWorkExperiencesQuery query =
        mapToGetWorkExperiencesQuery(
            securityContext,
            scope,
            jobTitle,
            company,
            technologies,
            startDate,
            endDate,
            minSalary,
            maxSalary);
    workExperienceService
        .getWorkExperiences(query)
        .thenApply(WorkExperienceResource::toResponse)
        .exceptionally(ExceptionMapper::map)
        .thenAccept(asyncResponse::resume);
  }

  @PUT
  @Path("/{workExperienceId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public void updateWorkExperience(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @PathParam("workExperienceId") String workExperienceId,
      WorkExperienceDto requestDto) {
    UserId id = getUserId(securityContext);
    workExperienceService
        .updateWorkExperience(
            mapToUpdateWorkExperienceCommand(UUID.fromString(workExperienceId), id, requestDto))
        .thenApply(ignore -> Response.ok().build())
        .exceptionally(ExceptionMapper::map)
        .thenAccept(asyncResponse::resume);
  }

  @DELETE
  @Path("/{workExperienceId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public void deleteWorkExperience(
      @Context SecurityContext securityContext,
      @Suspended final AsyncResponse asyncResponse,
      @PathParam("workExperienceId") String workExperienceId) {

    DeleteWorkExperienceCommand command = new DeleteWorkExperienceCommand();
    command.id = UUID.fromString(workExperienceId);
    command.userId = getUserId(securityContext);
    workExperienceService
        .deleteWorkExperience(command)
        .thenApply(ignore -> Response.ok().build())
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
            workExperiences.stream().map(WorkExperienceDto::of).collect(Collectors.toList()))
        .build();
  }

  private static CreateWorkExperienceCommand mapToCreateWorkExperienceCommand(
      UserId userId, WorkExperienceDto dto) {
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
    command.salary =
        toWorkExperienceField(
            dto.salary, salaryDto -> Money.of(salaryDto.value, salaryDto.currency));
    return command;
  }

  private static UpdateWorkExperienceCommand mapToUpdateWorkExperienceCommand(
      UUID workExperienceId, UserId userId, WorkExperienceDto dto) {
    UpdateWorkExperienceCommand command = new UpdateWorkExperienceCommand();
    command.id = workExperienceId;
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
    command.salary =
        toWorkExperienceField(
            dto.salary, salaryDto -> Money.of(salaryDto.value, salaryDto.currency));
    return command;
  }

  private static GetWorkExperiencesQuery mapToGetWorkExperiencesQuery(
      SecurityContext securityContext,
      String scope,
      String jobTitle,
      String company,
      Set<String> technologies,
      String startDate,
      String endDate,
      BigDecimal minSalary,
      BigDecimal maxSalary) {
    UserId userId = getUserId(securityContext);
    GetWorkExperiencesQuery query = new GetWorkExperiencesQuery();
    query.userId = userId;
    query.scope = GetWorkExperiencesQuery.Scope.of(scope);
    query.jobTitle = Optional.ofNullable(jobTitle).map(JobTitle::of).orElse(null);
    query.company = Optional.ofNullable(company).map(Company::of).orElse(null);
    query.technologies =
        Optional.ofNullable(technologies)
            .map(Collection::stream)
            .map(a -> a.map(Technology::of).collect(Collectors.toSet()))
            .orElse(null);
    query.startDate = LocalDate.parse(startDate, FORMATTER);
    query.endDate = LocalDate.parse(endDate, FORMATTER);
    query.minSalary = Money.of(minSalary, CurrencyContext.getPrototypeAllowedCurrency());
    query.maxSalary = Money.of(maxSalary, CurrencyContext.getPrototypeAllowedCurrency());
    return query;
  }

  private static <R, S extends WorkExperienceFieldDto<R>, T>
      RestrictedField<T> toWorkExperienceField(S dto, Function<R, T> mappingFunction) {
    return dto.isPublic
        ? RestrictedField.ofPublic((mappingFunction.apply(dto.content)))
        : RestrictedField.ofPrivate((mappingFunction.apply(dto.content)));
  }
}
