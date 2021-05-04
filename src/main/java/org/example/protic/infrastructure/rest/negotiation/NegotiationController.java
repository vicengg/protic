package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.application.negotiation.*;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.rest.ExceptionMapper;
import org.example.protic.infrastructure.rest.IdResponseDto;
import org.example.protic.infrastructure.rest.RestControllerUtils;
import org.example.protic.infrastructure.rest.RestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
      value = "/{negotiationId}")
  public CompletableFuture<ResponseEntity<RestDto>> updateNegotiation(
      @PathVariable("negotiationId") String negotiationId,
      @RequestBody NegotiationDto negotiationDto) {
    User user = getUser();
    return negotiationService
        .updateNegotiation(
            mapToUpdateNegotiationCommand(user, UUID.fromString(negotiationId), negotiationDto))
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{negotiationId}/accept")
  public CompletableFuture<ResponseEntity<RestDto>> acceptNegotiation(
      @PathVariable("negotiationId") String negotiationId) {
    User user = getUser();
    return negotiationService
        .acceptNegotiation(mapToAcceptNegotiationCommand(user, UUID.fromString(negotiationId)))
        .thenApply(RestControllerUtils::toOkResponse)
        .exceptionally(ExceptionMapper::map);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/{negotiationId}/cancel")
  public CompletableFuture<ResponseEntity<RestDto>> cancelNegotiation(
      @PathVariable("negotiationId") String negotiationId) {
    User user = getUser();
    return negotiationService
        .cancelNegotiation(mapToCancelNegotiationCommand(user, UUID.fromString(negotiationId)))
        .thenApply(RestControllerUtils::toOkResponse)
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

  private static CreateNegotiationCommand mapToCreateNegotiationCommand(
      User user, NegotiationDto dto) {
    CreateNegotiationCommand command = new CreateNegotiationCommand();
    command.user = user;
    command.offeredWorkExperienceId = UUID.fromString(dto.offeredWorkExperienceId);
    command.demandedWorkExperienceId = UUID.fromString(dto.demandedWorkExperienceId);
    command.offeredData = toVisibilityRequest(dto.offeredData);
    command.demandedData = toVisibilityRequest(dto.demandedData);
    return command;
  }

  private static UpdateNegotiationCommand mapToUpdateNegotiationCommand(
      User user, UUID negotiationId, NegotiationDto dto) {
    UpdateNegotiationCommand command = new UpdateNegotiationCommand();
    command.user = user;
    command.negotiationId = negotiationId;
    command.offeredData = toVisibilityRequest(dto.offeredData);
    command.demandedData = toVisibilityRequest(dto.demandedData);
    return command;
  }

  private static AcceptNegotiationCommand mapToAcceptNegotiationCommand(
      User user, UUID negotiationId) {
    AcceptNegotiationCommand command = new AcceptNegotiationCommand();
    command.user = user;
    command.negotiationId = negotiationId;
    return command;
  }

  private static CancelNegotiationCommand mapToCancelNegotiationCommand(
      User user, UUID negotiationId) {
    CancelNegotiationCommand command = new CancelNegotiationCommand();
    command.user = user;
    command.negotiationId = negotiationId;
    return command;
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
