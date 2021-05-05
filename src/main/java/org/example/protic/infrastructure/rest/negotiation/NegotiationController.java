package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.application.negotiation.*;
import org.example.protic.domain.negotiation.Action;
import org.example.protic.domain.negotiation.NegotiationProjection;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/negotiation")
public class NegotiationController {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final NegotiationService negotiationService;

  @Autowired
  public NegotiationController(NegotiationService negotiationService) {
    this.negotiationService = negotiationService;
  }

  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> createNegotiation(
      @RequestBody NegotiationDto negotiationDto) {
    User user = getUser();
    return negotiationService
        .createNegotiation(mapToCreateNegotiationCommand(user, negotiationDto))
        .thenApply(NegotiationController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{negotiationId}/action")
  public CompletableFuture<ResponseEntity<RestDto>> updateNegotiation(
      @PathVariable("negotiationId") String negotiationId, @RequestBody ActionDto actionDto) {
    User user = getUser();
    return negotiationService
        .updateNegotiation(
            mapToUpdateNegotiationCommand(user, UUID.fromString(negotiationId), actionDto))
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public CompletableFuture<ResponseEntity<RestDto>> getNegotiations(
      @RequestParam(value = "scope", defaultValue = "creator") String scope) {
    GetNegotiationsQuery query = new GetNegotiationsQuery();
    query.user = getUser();
    query.scope = GetNegotiationsQuery.Scope.of(scope);
    return negotiationService
        .getNegotiations(query)
        .thenApply(NegotiationController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{negotiationId}")
  public CompletableFuture<ResponseEntity<RestDto>> getNegotiation(
      @PathVariable("negotiationId") String negotiationId) {
    GetNegotiationQuery query = new GetNegotiationQuery();
    query.user = getUser();
    query.negotiationId = UUID.fromString(negotiationId);
    return negotiationService
        .getNegotiation(query)
        .thenApply(NegotiationController::toResponse)
        .exceptionally(ExceptionMapper::map);
  }

  private static User getUser() {
    return User.of(
        Objects.requireNonNull(RestControllerUtils.getUser().getAttribute("id")).toString(),
        Objects.requireNonNull(RestControllerUtils.getUser().getAttribute("login")).toString(),
        Optional.ofNullable(RestControllerUtils.getUser().getAttribute("avatar_url"))
            .map(Object::toString)
            .orElse(null));
  }

  private static ResponseEntity<RestDto> toResponse(UUID uuid) {
    return RestControllerUtils.toOkResponse(IdResponseDto.of(uuid));
  }

  private static ResponseEntity<RestDto> toResponse(List<NegotiationProjection> negotiations) {
    return RestControllerUtils.toOkResponse(
        CollectionDto.of(
            negotiations.stream().map(NegotiationDto::of).collect(Collectors.toList())));
  }

  private static ResponseEntity<RestDto> toResponse(NegotiationProjection negotiation) {
    return RestControllerUtils.toOkResponse(NegotiationDto.of(negotiation));
  }

  private static CreateNegotiationCommand mapToCreateNegotiationCommand(
      User user, NegotiationDto dto) {
    CreateNegotiationCommand command = new CreateNegotiationCommand();
    command.user = user;
    command.offeredWorkExperienceId = UUID.fromString(dto.offeredWorkExperienceId);
    command.demandedWorkExperienceId = UUID.fromString(dto.demandedWorkExperienceId);
    return command;
  }

  private static UpdateNegotiationCommand mapToUpdateNegotiationCommand(
      User user, UUID negotiationId, ActionDto actionDto) {
    UpdateNegotiationCommand command = new UpdateNegotiationCommand();
    command.user = user;
    command.negotiationId = negotiationId;
    command.action = toAction(actionDto, user);
    return command;
  }

  private static Action toAction(ActionDto actionDto, User issuer) {
    return Action.of(
        Action.Type.of(actionDto.type),
        issuer,
        toVisibilityRequest(actionDto.offeredData),
        toVisibilityRequest(actionDto.demandedData));
  }

  private static VisibilityRequest toVisibilityRequest(VisibilityRequestDto visibilityRequestDto) {
    return VisibilityRequest.builder()
        .withJobTitle(toVisibility(visibilityRequestDto.jobTitle))
        .withCompany(toVisibility(visibilityRequestDto.company))
        .withTechnologies(toVisibility(visibilityRequestDto.technologies))
        .withWorkPeriod(toVisibility(visibilityRequestDto.workPeriod))
        .withSalary(toVisibility(visibilityRequestDto.salary))
        .build();
  }

  private static Visibility toVisibility(String key) {
    return Optional.ofNullable(key).map(Visibility::of).orElse(Visibility.KEEP_PRIVATE);
  }
}
