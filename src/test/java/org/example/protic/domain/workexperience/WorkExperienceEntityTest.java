package org.example.protic.domain.workexperience;

import org.apache.commons.collections4.SetUtils;
import org.example.protic.commons.ValidationException;
import org.example.protic.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkExperienceEntityTest {

  private static final UserId USER_ID = UserId.of("user_id");
  private static final UserId ANOTHER_USER_ID = UserId.of("another_user_id");
  private static final WorkExperienceField<JobTitle> JOB_TITLE_PRIVATE =
      WorkExperienceField.ofPrivate(JobTitle.of("job title private"));
  private static final WorkExperienceField<Company> COMPANY_PRIVATE =
      WorkExperienceField.ofPrivate(Company.of("company private"));
  private static final WorkExperienceField<Set<Technology>> TECHNOLOGIES_PRIVATE =
      WorkExperienceField.ofPrivate(Set.of(Technology.of("technology private")));
  public static final WorkExperienceField<WorkPeriod> WORK_PERIOD_PRIVATE =
      WorkExperienceField.ofPrivate(
          WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent());
  private static final WorkExperienceField<JobTitle> JOB_TITLE_PUBLIC =
      WorkExperienceField.ofPublic(JobTitle.of("job title public"));
  private static final WorkExperienceField<Company> COMPANY_PUBLIC =
      WorkExperienceField.ofPublic(Company.of("company public"));
  private static final WorkExperienceField<Set<Technology>> TECHNOLOGIES_PUBLIC =
      WorkExperienceField.ofPublic(Set.of(Technology.of("technology public")));
  public static final WorkExperienceField<WorkPeriod> WORK_PERIOD_PUBLIC =
      WorkExperienceField.ofPublic(
          WorkPeriod.from(LocalDate.now().minus(2, ChronoUnit.DAYS)).toPresent());

  @Test
  @DisplayName("It tries to create a work experience entity without user id.")
  void createWorkExperienceEntityWithoutUserId() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder(null, true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
                    .build());
    assertEquals("User ID is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a work experience entity without job title.")
  void createWorkExperienceEntityWithoutJobTitle() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder(USER_ID, true)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
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
                WorkExperienceEntity.builder(USER_ID, true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
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
                WorkExperienceEntity.builder(USER_ID, true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
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
                WorkExperienceEntity.builder(USER_ID, true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(WorkExperienceField.ofPrivate(SetUtils.emptySet()))
                    .withWorkPeriod(WORK_PERIOD_PRIVATE)
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
                WorkExperienceEntity.builder(USER_ID, true)
                    .withJobTitle(JOB_TITLE_PRIVATE)
                    .withCompany(COMPANY_PRIVATE)
                    .withTechnologies(TECHNOLOGIES_PRIVATE)
                    .build());
    assertEquals("Work period is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a work experience with all fields.")
  void createWorkExperienceEntityWithAllFields() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder(USER_ID, true)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .build();
    assertNotNull(workExperienceEntity.getId());
    assertNotNull(workExperienceEntity.getCreatedAt());
    assertEquals(USER_ID, workExperienceEntity.getUserId());
    assertTrue(workExperienceEntity.getBinding());
    assertEquals(JOB_TITLE_PRIVATE, workExperienceEntity.getJobTitle());
    assertEquals(COMPANY_PRIVATE, workExperienceEntity.getCompany());
    assertEquals(TECHNOLOGIES_PRIVATE, workExperienceEntity.getTechnologies());
    assertEquals(WORK_PERIOD_PRIVATE, workExperienceEntity.getWorkPeriod());
  }

  @Test
  @DisplayName("It copies a work experience entity.")
  void copyWorkExperienceEntity() {
    WorkExperienceEntity source =
        WorkExperienceEntity.builder(USER_ID, true)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .build();
    WorkExperienceEntity copy = WorkExperienceEntity.copy(source);
    assertEquals(source.getId(), copy.getId());
    assertEquals(source.getCreatedAt(), copy.getCreatedAt());
    assertEquals(source.getUserId(), copy.getUserId());
    assertEquals(source.getBinding(), copy.getBinding());
    assertEquals(source.getJobTitle(), copy.getJobTitle());
    assertEquals(source.getCompany(), copy.getCompany());
    assertEquals(source.getTechnologies(), copy.getTechnologies());
    assertEquals(source.getWorkPeriod(), copy.getWorkPeriod());
  }

  @Test
  @DisplayName("It create a work experience projection with every field public.")
  void toWorkExperienceProjectionWithEveryFieldPublic() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder(USER_ID, true)
            .withJobTitle(JOB_TITLE_PUBLIC)
            .withCompany(COMPANY_PUBLIC)
            .withTechnologies(TECHNOLOGIES_PUBLIC)
            .withWorkPeriod(WORK_PERIOD_PUBLIC)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER_ID);
    assertTrue(workExperienceProjection.getUserId().isPresent());
    assertEquals(USER_ID, workExperienceProjection.getUserId().get());
    assertTrue(workExperienceProjection.getJobTitle().isPresent());
    assertEquals(JOB_TITLE_PUBLIC.getValue(), workExperienceProjection.getJobTitle().get());
    assertTrue(workExperienceProjection.getCompany().isPresent());
    assertEquals(COMPANY_PUBLIC.getValue(), workExperienceProjection.getCompany().get());
    assertTrue(workExperienceProjection.getTechnologies().isPresent());
    assertEquals(TECHNOLOGIES_PUBLIC.getValue(), workExperienceProjection.getTechnologies().get());
    assertTrue(workExperienceProjection.getWorkPeriod().isPresent());
    assertEquals(WORK_PERIOD_PUBLIC.getValue(), workExperienceProjection.getWorkPeriod().get());
  }

  @Test
  @DisplayName(
      "It create a work experience projection with every field private for the owner user.")
  void toWorkExperienceProjectionWithEveryFieldPrivateOwnerUser() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder(USER_ID, false)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER_ID);
    assertTrue(workExperienceProjection.getUserId().isPresent());
    assertEquals(USER_ID, workExperienceProjection.getUserId().get());
    assertTrue(workExperienceProjection.getJobTitle().isPresent());
    assertEquals(JOB_TITLE_PRIVATE.getValue(), workExperienceProjection.getJobTitle().get());
    assertTrue(workExperienceProjection.getCompany().isPresent());
    assertEquals(COMPANY_PRIVATE.getValue(), workExperienceProjection.getCompany().get());
    assertTrue(workExperienceProjection.getTechnologies().isPresent());
    assertEquals(TECHNOLOGIES_PRIVATE.getValue(), workExperienceProjection.getTechnologies().get());
    assertTrue(workExperienceProjection.getWorkPeriod().isPresent());
    assertEquals(WORK_PERIOD_PRIVATE.getValue(), workExperienceProjection.getWorkPeriod().get());
  }

  @Test
  @DisplayName(
      "It create a work experience projection with every field private for the non-owner user.")
  void toWorkExperienceProjectionWithEveryFieldPrivateNonOwnerUser() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder(ANOTHER_USER_ID, false)
            .withJobTitle(JOB_TITLE_PRIVATE)
            .withCompany(COMPANY_PRIVATE)
            .withTechnologies(TECHNOLOGIES_PRIVATE)
            .withWorkPeriod(WORK_PERIOD_PRIVATE)
            .build();
    WorkExperienceProjection workExperienceProjection =
        workExperienceEntity.toWorkExperienceResponse(USER_ID);
    assertFalse(workExperienceProjection.getUserId().isPresent());
    assertFalse(workExperienceProjection.getJobTitle().isPresent());
    assertFalse(workExperienceProjection.getCompany().isPresent());
    assertFalse(workExperienceProjection.getTechnologies().isPresent());
    assertFalse(workExperienceProjection.getWorkPeriod().isPresent());
  }
}
