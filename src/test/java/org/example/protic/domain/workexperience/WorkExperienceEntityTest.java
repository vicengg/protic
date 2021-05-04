package org.example.protic.domain.workexperience;

import org.apache.commons.collections4.SetUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.user.User;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkExperienceEntityTest {

  private static final User USER = User.of("user_id", "", "");
  private static final User ANOTHER_USER = User.of("another_user_id", "", "");
  private static final RestrictedField<JobTitle> JOB_TITLE_PRIVATE =
      RestrictedField.ofPrivate(JobTitle.of("job title private"));
  private static final RestrictedField<Company> COMPANY_PRIVATE =
      RestrictedField.ofPrivate(Company.of("company private"));
  private static final RestrictedField<Set<Technology>> TECHNOLOGIES_PRIVATE =
      RestrictedField.ofPrivate(Set.of(Technology.of("technology private")));
  public static final RestrictedField<WorkPeriod> WORK_PERIOD_PRIVATE =
      RestrictedField.ofPrivate(
          WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent());
  private static final RestrictedField<Money> SALARY_PRIVATE =
      RestrictedField.ofPrivate(Money.of(1000, "EUR"));
  private static final RestrictedField<JobTitle> JOB_TITLE_PUBLIC =
      RestrictedField.ofPublic(JobTitle.of("job title public"));
  private static final RestrictedField<Company> COMPANY_PUBLIC =
      RestrictedField.ofPublic(Company.of("company public"));
  private static final RestrictedField<Set<Technology>> TECHNOLOGIES_PUBLIC =
      RestrictedField.ofPublic(Set.of(Technology.of("technology public")));
  public static final RestrictedField<WorkPeriod> WORK_PERIOD_PUBLIC =
      RestrictedField.ofPublic(
          WorkPeriod.from(LocalDate.now().minus(2, ChronoUnit.DAYS)).toPresent());
  private static final RestrictedField<Money> SALARY_PUBLIC =
      RestrictedField.ofPrivate(Money.of(2000, "EUR"));
  private static final RestrictedField<Money> INVALID_CURRENCY_SALARY =
      RestrictedField.ofPrivate(Money.of(1000, "CZK"));

  @Test
  @DisplayName("It tries to create a work experience entity without user.")
  void createWorkExperienceEntityWithoutUser() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(null)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals("User is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without job title.")
  void createWorkExperienceEntityWithoutJobTitle() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals("Job title is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without company.")
  void createWorkExperienceEntityWithoutCompany() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals("Company is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without technologies.")
  void createWorkExperienceEntityWithoutTechnologies() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals(
        "A list of technologies is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity with empty technologies list.")
  void createWorkExperienceEntityWithEmptyTechnologiesList() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(RestrictedField.ofPrivate(SetUtils.emptySet()))
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals(
        "The technologies list must contain at least one element.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without work period.")
  void createWorkExperienceEntityWithoutWorkPeriod() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withSalary(SALARY_PRIVATE)
                    .build());
    assertEquals("Work period is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without salary.")
  void createWorkExperienceEntityWithoutSalary() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .build());
    assertEquals("Salary is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience with an invalid currency.")
  void createWorkExperienceEntityWithAnInvalidCurrency() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withUser(USER)
                    .withBinding(true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .withSalary(INVALID_CURRENCY_SALARY)
                    .build());
    assertEquals("Not valid currency: CZK", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a work experience with all fields.")
  void createWorkExperienceEntityWithAllFields() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder()
            .withUser(USER)
            .withBinding(true)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .withSalary(SALARY_PRIVATE)
            .build();
    assertNotNull(workExperienceEntity.getId());
    assertNotNull(workExperienceEntity.getCreatedAt());
    assertEquals(USER, workExperienceEntity.getUser());
    assertTrue(workExperienceEntity.getBinding());
    assertEquals(JOB_TITLE_PRIVATE, workExperienceEntity.getJobTitle());
    assertEquals(COMPANY_PRIVATE, workExperienceEntity.getCompany());
    assertEquals(TECHNOLOGIES_PRIVATE, workExperienceEntity.getTechnologies());
    assertEquals(WORK_PERIOD_PRIVATE, workExperienceEntity.getWorkPeriod());
    assertEquals(SALARY_PRIVATE, workExperienceEntity.getSalary());
  }

  @Test
  @DisplayName("It copies a work experience entity.")
  void copyWorkExperienceEntity() {
    WorkExperienceEntity source =
        WorkExperienceEntity.builder()
            .withUser(USER)
            .withBinding(true)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .withSalary(SALARY_PRIVATE)
            .build();
    WorkExperienceEntity copy = WorkExperienceEntity.copy(source);
    assertEquals(source.getId(), copy.getId());
    assertEquals(source.getCreatedAt(), copy.getCreatedAt());
    assertEquals(source.getUser(), copy.getUser());
    assertEquals(source.getBinding(), copy.getBinding());
    assertEquals(source.getJobTitle(), copy.getJobTitle());
    assertEquals(source.getCompany(), copy.getCompany());
    assertEquals(source.getTechnologies(), copy.getTechnologies());
    assertEquals(source.getWorkPeriod(), copy.getWorkPeriod());
    assertEquals(source.getSalary(), copy.getSalary());
  }

  @Test
  @DisplayName("It create a work experience projection with every field public.")
  void toWorkExperienceProjectionWithEveryFieldPublic() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder()
            .withUser(USER)
            .withBinding(true)
            .withJobTitle(JOB_TITLE_PUBLIC)
            .withCompany(COMPANY_PUBLIC)
            .withTechnologies(TECHNOLOGIES_PUBLIC)
            .withWorkPeriod(WORK_PERIOD_PUBLIC)
            .withSalary(SALARY_PUBLIC)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER);
    assertTrue(workExperienceProjection.getUser().isPresent());
    assertEquals(USER, workExperienceProjection.getUser().get());
    assertTrue(workExperienceProjection.getJobTitle().isPresent());
    assertEquals(JOB_TITLE_PUBLIC, workExperienceProjection.getJobTitle().get());
    assertTrue(workExperienceProjection.getCompany().isPresent());
    assertEquals(COMPANY_PUBLIC, workExperienceProjection.getCompany().get());
    assertTrue(workExperienceProjection.getTechnologies().isPresent());
    assertEquals(TECHNOLOGIES_PUBLIC, workExperienceProjection.getTechnologies().get());
    assertTrue(workExperienceProjection.getWorkPeriod().isPresent());
    assertEquals(WORK_PERIOD_PUBLIC, workExperienceProjection.getWorkPeriod().get());
    assertTrue(workExperienceProjection.getSalary().isPresent());
    assertEquals(SALARY_PUBLIC, workExperienceProjection.getSalary().get());
  }

  @Test
  @DisplayName(
      "It create a work experience projection with every field private for the owner user.")
  void toWorkExperienceProjectionWithEveryFieldPrivateOwnerUser() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder()
            .withUser(USER)
            .withBinding(true)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .withSalary(SALARY_PRIVATE)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER);
    assertTrue(workExperienceProjection.getUser().isPresent());
    assertEquals(USER, workExperienceProjection.getUser().get());
    assertTrue(workExperienceProjection.getJobTitle().isPresent());
    assertEquals(JOB_TITLE_PRIVATE, workExperienceProjection.getJobTitle().get());
    assertTrue(workExperienceProjection.getCompany().isPresent());
    assertEquals(COMPANY_PRIVATE, workExperienceProjection.getCompany().get());
    assertTrue(workExperienceProjection.getTechnologies().isPresent());
    assertEquals(TECHNOLOGIES_PRIVATE, workExperienceProjection.getTechnologies().get());
    assertTrue(workExperienceProjection.getWorkPeriod().isPresent());
    assertEquals(WORK_PERIOD_PRIVATE, workExperienceProjection.getWorkPeriod().get());
    assertTrue(workExperienceProjection.getSalary().isPresent());
    assertEquals(SALARY_PRIVATE, workExperienceProjection.getSalary().get());
  }

  @Test
  @DisplayName(
      "It create a work experience projection with every field private for the non-owner user.")
  void toWorkExperienceProjectionWithEveryFieldPrivateNonOwnerUser() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder()
            .withUser(ANOTHER_USER)
            .withBinding(false)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .withSalary(SALARY_PRIVATE)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER);
    assertFalse(workExperienceProjection.getUser().isPresent());
    assertFalse(workExperienceProjection.getJobTitle().isPresent());
    assertFalse(workExperienceProjection.getCompany().isPresent());
    assertFalse(workExperienceProjection.getTechnologies().isPresent());
    assertFalse(workExperienceProjection.getWorkPeriod().isPresent());
    assertFalse(workExperienceProjection.getSalary().isPresent());
  }
}
