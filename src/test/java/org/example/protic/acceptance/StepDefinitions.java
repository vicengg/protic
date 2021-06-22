package org.example.protic.acceptance;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.protic.application.negotiation.*;
import org.example.protic.application.workexperience.*;
import org.example.protic.domain.negotiation.Action;
import org.example.protic.domain.negotiation.Visibility;
import org.example.protic.domain.negotiation.VisibilityRequest;
import org.example.protic.domain.user.User;
import org.example.protic.domain.workexperience.*;
import org.javamoney.moneta.Money;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class StepDefinitions {

  private static final Map<String, User> USERS = new HashMap<>();
  private static final Map<String, CreateWorkExperienceCommand> CREATE_WORK_EXPERIENCE_COMMANDS =
      new HashMap<>();
  private static final Map<String, UUID> WORK_EXPERIENCE_REFERENCES = new HashMap<>();

  private final WorkExperienceRepository workExperienceRepository =
      new TestingWorkExperienceRepository();
  private final NegotiationRepository negotiationRepository = new TestingNegotiationRepository();
  private final WorkExperienceService workExperienceService =
      new WorkExperienceServiceImpl(workExperienceRepository, negotiationRepository);
  private final NegotiationService negotiationService =
      new NegotiationServiceImpl(workExperienceRepository, negotiationRepository);

  private List<WorkExperienceProjection> focusedWorkExperiences;
  private WorkExperienceProjection focusedWorkExperience;
  private UUID negotiationId;
  private UpdateNegotiationCommand updateNegotiationCommand;
  private VisibilityRequest offeredVisibility;
  private VisibilityRequest demandedVisibility;

  @Given("a user with id {string}")
  public void createUser(String userId) {
    USERS.put(userId, User.of(userId, userId, null));
  }

  @And("the user {string} starts to fill the create work experience form")
  public void createWorkExperienceCreationCommand(String userId) {
    CreateWorkExperienceCommand command = new CreateWorkExperienceCommand();
    command.user = USERS.get(userId);
    CREATE_WORK_EXPERIENCE_COMMANDS.put(userId, command);
  }

  @And("the user {string} fills the job title as {string} with {string} visibility")
  public void addJobTitleToWorkExperienceCreationCommand(
      String userId, String jobTitle, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.jobTitle = createField(jobTitle, visibility, JobTitle::of);
  }

  @And("the user {string} fills the company as {string} with {string} visibility")
  public void addCompanyToWorkExperienceCreationCommand(
      String userId, String company, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.company = createField(company, visibility, Company::of);
  }

  @And("the user {string} adds a technology {string} with {string} visibility")
  public void addTechnologyToWorkExperienceCreationCommand(
      String userId, String technology, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.technologies = createField(technology, visibility, name -> Set.of(Technology.of(name)));
  }

  @And("the user {string} adds a work period from {string} to {string} with {string} visibility")
  public void addWorkPeriodToWorkExperienceCreationCommand(
      String userId, String startDate, String endDate, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.workPeriod =
        createField(
            WorkPeriod.from(LocalDate.parse(startDate)).to(LocalDate.parse(endDate)),
            visibility,
            Function.identity());
  }

  @And("the user {string} adds a work period from {string} to present with {string} visibility")
  public void addUnfinishedWorkPeriodToWorkExperienceCreationCommand(
      String userId, String startDate, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.workPeriod =
        createField(
            WorkPeriod.from(LocalDate.parse(startDate)).toPresent(),
            visibility,
            Function.identity());
  }

  @And("the user {string} adds a salary of {int} {string} with {string} visibility")
  public void addSalaryToWorkExperienceCreationCommand(
      String userId, int salary, String currency, String visibility) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.salary = createField(Money.of(salary, currency), visibility, Function.identity());
  }

  @And(
      "the user {string} sends his work experience to create, associated to his profile, with reference ID {string}")
  public void createWorkExperienceBinded(String userId, String workExperienceReference) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.binding = true;
    WORK_EXPERIENCE_REFERENCES.put(
        workExperienceReference, workExperienceService.createWorkExperience(command).join());
  }

  @And(
      "the user {string} sends his work experience to create, anonymously, with reference ID {string}")
  public void createWorkExperienceNotBinded(String userId, String workExperienceReference) {
    CreateWorkExperienceCommand command = CREATE_WORK_EXPERIENCE_COMMANDS.get(userId);
    command.binding = false;
    WORK_EXPERIENCE_REFERENCES.put(
        workExperienceReference, workExperienceService.createWorkExperience(command).join());
  }

  @When("the user {string} gets his own work experiences")
  public void getOwnWorkExperiences(String userId) {
    GetWorkExperiencesQuery query = new GetWorkExperiencesQuery();
    query.user = USERS.get(userId);
    query.scope = GetWorkExperiencesQuery.Scope.OWN;
    focusedWorkExperiences = workExperienceService.getWorkExperiences(query).join();
  }

  @When("the user {string} gets work experiences of others")
  public void getForeignWorkExperiences(String userId) {
    GetWorkExperiencesQuery query = new GetWorkExperiencesQuery();
    query.user = USERS.get(userId);
    query.scope = GetWorkExperiencesQuery.Scope.FOREIGN;
    focusedWorkExperiences = workExperienceService.getWorkExperiences(query).join();
  }

  @Then("the work experience {string} is present")
  public void checkWorkExperienceIsPresent(String workExperienceReference) {
    focusedWorkExperience =
        focusedWorkExperiences.stream()
            .filter(
                we -> we.getId().equals(WORK_EXPERIENCE_REFERENCES.get(workExperienceReference)))
            .findFirst()
            .orElseThrow(AssertionError::new);
  }

  @Then("the work experience {string} is NOT present")
  public void checkWorkExperienceIsNotPresent(String workExperienceReference) {
    if (focusedWorkExperiences.stream()
        .anyMatch(
            we -> we.getId().equals(WORK_EXPERIENCE_REFERENCES.get(workExperienceReference)))) {
      throw new AssertionError();
    }
  }

  @And("the work experience job title is present")
  public void checkJobTitleIsPresent() {
    focusedWorkExperience.getJobTitle().orElseThrow(AssertionError::new);
  }

  @And("the work experience company is present")
  public void checkCompanyIsPresent() {
    focusedWorkExperience.getCompany().orElseThrow(AssertionError::new);
  }

  @And("the work experience technologies are present")
  public void checkTechnologiesIsPresent() {
    focusedWorkExperience.getTechnologies().orElseThrow(AssertionError::new);
  }

  @And("the work experience work period is present")
  public void checkWorkPeriodIsPresent() {
    focusedWorkExperience.getWorkPeriod().orElseThrow(AssertionError::new);
  }

  @And("the work experience salary is present")
  public void checkSalaryIsPresent() {
    focusedWorkExperience.getSalary().orElseThrow(AssertionError::new);
  }

  @And("the work experience user is NOT anonymous")
  public void checkUserIsNotAnonymous() {
    focusedWorkExperience.getUser().orElseThrow(AssertionError::new);
  }

  @And("the work experience job title is NOT present")
  public void checkJobTitleIsNotPresent() {
    if (focusedWorkExperience.getJobTitle().isPresent()) {
      throw new AssertionError();
    }
  }

  @And("the work experience company is NOT present")
  public void checkCompanyIsNotPresent() {
    if (focusedWorkExperience.getCompany().isPresent()) {
      throw new AssertionError();
    }
  }

  @And("the work experience technologies are NOT present")
  public void checkTechnologiesIsNotPresent() {
    if (focusedWorkExperience.getTechnologies().isPresent()) {
      throw new AssertionError();
    }
  }

  @And("the work experience work period is NOT present")
  public void checkWorkPeriodIsNotPresent() {
    if (focusedWorkExperience.getWorkPeriod().isPresent()) {
      throw new AssertionError();
    }
  }

  @And("the work experience salary is NOT present")
  public void checkSalaryIsNotPresent() {
    if (focusedWorkExperience.getSalary().isPresent()) {
      throw new AssertionError();
    }
  }

  @And("the work experience user is anonymous")
  public void checkUserIsAnonymous() {
    if (focusedWorkExperience.getUser().isPresent()) {
      throw new AssertionError();
    }
  }

  @When("the user {string} creates a data request to unlock {string} offering {string}")
  public void createNegotiationCommand(
      String userId, String demandedWorkExperienceId, String offeredWorkExperienceId) {
    CreateNegotiationCommand createNegotiationCommand = new CreateNegotiationCommand();
    createNegotiationCommand.user = USERS.get(userId);
    createNegotiationCommand.demandedWorkExperienceId =
        WORK_EXPERIENCE_REFERENCES.get(demandedWorkExperienceId);
    createNegotiationCommand.offeredWorkExperienceId =
        WORK_EXPERIENCE_REFERENCES.get(offeredWorkExperienceId);
    negotiationId = negotiationService.createNegotiation(createNegotiationCommand).join();
  }

  @When("the user {string} adds a negotiation step")
  public void addNegotiationStep(String userId) {
    updateNegotiationCommand = new UpdateNegotiationCommand();
    updateNegotiationCommand.negotiationId = negotiationId;
    updateNegotiationCommand.user = USERS.get(userId);
  }

  @And("asks for visibility for {string}")
  public void asksForVisibility(String fields) {
    demandedVisibility =
        VisibilityRequest.builder()
            .withJobTitle(
                fields.contains("job title") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withWorkPeriod(
                fields.contains("work period") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withCompany(
                fields.contains("company") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withTechnologies(
                fields.contains("technologies") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withSalary(
                fields.contains("salary") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .build();
  }

  @And("offers visibility for {string}")
  public void offersForVisibility(String fields) {
    offeredVisibility =
        VisibilityRequest.builder()
            .withJobTitle(
                fields.contains("job title") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withWorkPeriod(
                fields.contains("work period") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withCompany(
                fields.contains("company") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withTechnologies(
                fields.contains("technologies") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .withSalary(
                fields.contains("salary") ? Visibility.MAKE_PUBLIC : Visibility.KEEP_PRIVATE)
            .build();
  }

  @And("{string} sends the negotiation")
  public void sendNegotiation(String userId) {
    updateNegotiationCommand.action =
        Action.of(
            Action.Type.MODIFY,
            USERS.get(userId),
            Timestamp.from(Instant.now()),
            offeredVisibility,
            demandedVisibility);
    negotiationService.updateNegotiation(updateNegotiationCommand).join();
  }

  @And("{string} accepts the negotiation")
  public void acceptsNegotiation(String userId) {
    updateNegotiationCommand = new UpdateNegotiationCommand();
    updateNegotiationCommand.negotiationId = negotiationId;
    updateNegotiationCommand.user = USERS.get(userId);
    updateNegotiationCommand.action =
        Action.of(
            Action.Type.ACCEPT,
            USERS.get(userId),
            Timestamp.from(Instant.now()),
            offeredVisibility,
            demandedVisibility);
    negotiationService.updateNegotiation(updateNegotiationCommand).join();
  }

  private static <S, T> RestrictedField<T> createField(
      S value, String visibility, Function<S, T> mappingFunction) {
    switch (visibility) {
      case "private":
        return RestrictedField.ofPrivate(mappingFunction.apply(value));
      case "public":
        return RestrictedField.ofPublic(mappingFunction.apply(value));
      default:
        throw new RuntimeException("Wrong visibility ID in feature definition.");
    }
  }
}
