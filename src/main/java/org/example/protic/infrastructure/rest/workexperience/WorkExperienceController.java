package org.example.protic.infrastructure.rest.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.application.workexperience.*;
import org.example.protic.commons.CurrencyContext;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.*;
import org.example.protic.infrastructure.rest.*;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/work-experience")
public class WorkExperienceController {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final WorkExperienceService workExperienceService;

  @Autowired
  public WorkExperienceController(WorkExperienceService workExperienceService) {
    this.workExperienceService = workExperienceService;
  }

  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> createWorkExperience(
      @RequestBody WorkExperienceDto requestDto) {
    User user = RestControllerUtils.getUser();
    return workExperienceService
        .createWorkExperience(mapToCreateWorkExperienceCommand(user, requestDto))
        .thenApply(WorkExperienceController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getWorkExperiences(
      @RequestParam(value = "scope", defaultValue = "all", required = false) String scope,
      @RequestParam(value = "jobTitle", required = false) String jobTitle,
      @RequestParam(value = "company", required = false) String company,
      @RequestParam(value = "technologies", required = false) Set<String> technologies,
      @RequestParam(value = "startDate", required = false) String startDate,
      @RequestParam(value = "endDate", required = false) String endDate,
      @RequestParam(value = "minSalary", required = false) BigDecimal minSalary,
      @RequestParam(value = "maxSalary", required = false) BigDecimal maxSalary) {
    GetWorkExperiencesQuery query =
        mapToGetWorkExperiencesQuery(
            scope, jobTitle, company, technologies, startDate, endDate, minSalary, maxSalary);
    return workExperienceService
        .getWorkExperiences(query)
        .thenApply(WorkExperienceController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      value = "/{workExperienceId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getWorkExperience(
      @PathVariable("workExperienceId") String workExperienceId) {
    GetWorkExperienceQuery query = new GetWorkExperienceQuery();
    query.id = UUID.fromString(workExperienceId);
    query.user = RestControllerUtils.getUser();
    return workExperienceService
        .getWorkExperience(query)
        .thenApply(WorkExperienceController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      value = "/{workExperienceId}",
      method = RequestMethod.PUT,
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> updateWorkExperience(
      @PathVariable("workExperienceId") String workExperienceId,
      @RequestBody WorkExperienceDto requestDto) {
    User user = RestControllerUtils.getUser();
    return workExperienceService
        .updateWorkExperience(
            mapToUpdateWorkExperienceCommand(UUID.fromString(workExperienceId), user, requestDto))
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      value = "/{workExperienceId}",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> deleteWorkExperience(
      @PathVariable("workExperienceId") String workExperienceId) {

    DeleteWorkExperienceCommand command = new DeleteWorkExperienceCommand();
    command.id = UUID.fromString(workExperienceId);
    command.user = RestControllerUtils.getUser();
    return workExperienceService
        .deleteWorkExperience(command)
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }

  private static ResponseEntity<RestDto> toResponse(UUID uuid) {
    return RestControllerUtils.toOkResponse(IdResponseDto.of(uuid));
  }

  private static ResponseEntity<RestDto> toResponse(
      List<WorkExperienceProjection> workExperiences) {
    return RestControllerUtils.toOkResponse(
        CollectionDto.of(
            workExperiences.stream().map(WorkExperienceDto::of).collect(Collectors.toList())));
  }

  private static ResponseEntity<RestDto> toResponse(WorkExperienceProjection workExperience) {
    return RestControllerUtils.toOkResponse(WorkExperienceDto.of(workExperience));
  }

  private static CreateWorkExperienceCommand mapToCreateWorkExperienceCommand(
      User user, WorkExperienceDto dto) {
    CreateWorkExperienceCommand command = new CreateWorkExperienceCommand();
    command.user = user;
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
      UUID workExperienceId, User user, WorkExperienceDto dto) {
    UpdateWorkExperienceCommand command = new UpdateWorkExperienceCommand();
    command.id = workExperienceId;
    command.user = user;
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
      String scope,
      String jobTitle,
      String company,
      Set<String> technologies,
      String startDate,
      String endDate,
      BigDecimal minSalary,
      BigDecimal maxSalary) {
    User user = RestControllerUtils.getUser();
    GetWorkExperiencesQuery query = new GetWorkExperiencesQuery();
    query.user = user;
    query.scope = GetWorkExperiencesQuery.Scope.of(scope);
    query.jobTitle =
        Optional.ofNullable(jobTitle)
            .map(WorkExperienceController::setNullOnEmpty)
            .map(JobTitle::of)
            .orElse(null);
    query.company =
        Optional.ofNullable(company)
            .map(WorkExperienceController::setNullOnEmpty)
            .map(Company::of)
            .orElse(null);
    query.technologies =
        Optional.ofNullable(technologies)
            .map(Collection::stream)
            .map(stream -> stream.filter(StringUtils::isNotBlank))
            .map(technology -> technology.map(Technology::of).collect(Collectors.toSet()))
            .orElse(null);
    query.startDate =
        Optional.ofNullable(startDate)
            .map(WorkExperienceController::setNullOnEmpty)
            .map(date -> LocalDate.parse(date, FORMATTER))
            .orElse(null);
    query.endDate =
        Optional.ofNullable(endDate)
            .map(WorkExperienceController::setNullOnEmpty)
            .map(date -> LocalDate.parse(date, FORMATTER))
            .orElse(null);
    query.minSalary =
        Optional.ofNullable(minSalary)
            .map(salary -> Money.of(salary, CurrencyContext.getPrototypeAllowedCurrency()))
            .orElse(null);
    query.maxSalary =
        Optional.ofNullable(maxSalary)
            .map(salary -> Money.of(salary, CurrencyContext.getPrototypeAllowedCurrency()))
            .orElse(null);
    return query;
  }

  private static <R, S extends WorkExperienceFieldDto<R>, T>
      RestrictedField<T> toWorkExperienceField(S dto, Function<R, T> mappingFunction) {
    return dto.isPublic
        ? RestrictedField.ofPublic((mappingFunction.apply(dto.content)))
        : RestrictedField.ofPrivate((mappingFunction.apply(dto.content)));
  }

  private static String setNullOnEmpty(final String text) {
    return text != null && text.trim().isEmpty() ? null : text;
  }
}
