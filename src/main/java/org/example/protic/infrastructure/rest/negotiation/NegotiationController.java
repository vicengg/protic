package org.example.protic.infrastructure.rest.negotiation;

import org.example.protic.application.negotiation.CreateNegotiationCommand;
import org.example.protic.application.negotiation.NegotiationService;
import org.example.protic.application.negotiation.UpdateNegotiationCommand;
import org.example.protic.domain.negotiation.Action;
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
      @RequestBody CreateNegotiationDto negotiationDto) {
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
      User user, CreateNegotiationDto dto) {
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
    return VisibilityRequest.builder(UUID.fromString(visibilityRequestDto.workExperienceId))
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
