package org.example.protic.domain.workexperience;

import org.apache.commons.collections4.SetUtils;
import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkExperienceEntityTest {

  private static final WorkExperienceField<JobTitle> JOB_TITLE =
      WorkExperienceField.ofPrivate(JobTitle.of("job title"));
  private static final WorkExperienceField<Company> COMPANY =
      WorkExperienceField.ofPrivate(Company.of("company"));
  private static final WorkExperienceField<Set<Technology>> TECHNOLOGIES =
      WorkExperienceField.ofPrivate(Set.of(Technology.of("technology")));
  public static final WorkExperienceField<WorkPeriod> WORK_PERIOD =
      WorkExperienceField.ofPrivate(
          WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent());

  @Test
  @DisplayName("It tries to create a work experience entity without job title.")
  void createWorkExperienceEntityWithoutJobTitle() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () ->
                WorkExperienceEntity.builder()
                    .withCompany(COMPANY)
                    .withTechnologies(TECHNOLOGIES)
                    .withWorkPeriod(WORK_PERIOD)
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
                    .withJobTitle(JOB_TITLE)
                    .withTechnologies(TECHNOLOGIES)
                    .withWorkPeriod(WORK_PERIOD)
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
                    .withJobTitle(JOB_TITLE)
                    .withCompany(COMPANY)
                    .withWorkPeriod(WORK_PERIOD)
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
                    .withJobTitle(JOB_TITLE)
                    .withCompany(COMPANY)
                    .withTechnologies(WorkExperienceField.ofPrivate(SetUtils.emptySet()))
                    .withWorkPeriod(WORK_PERIOD)
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
                    .withJobTitle(JOB_TITLE)
                    .withCompany(COMPANY)
                    .withTechnologies(TECHNOLOGIES)
                    .build());
    assertEquals("Work period is mandatory for work experience.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a work experience with all fields.")
  void createWorkExperienceEntityWithAllFields() {
    WorkExperienceEntity workExperienceEntity =
        WorkExperienceEntity.builder()
            .withJobTitle(JOB_TITLE)
            .withCompany(COMPANY)
            .withTechnologies(TECHNOLOGIES)
            .withWorkPeriod(WORK_PERIOD)
            .build();
    assertEquals(JOB_TITLE, workExperienceEntity.getJobTitle());
    assertEquals(COMPANY, workExperienceEntity.getCompany());
    assertEquals(TECHNOLOGIES, workExperienceEntity.getTechnologies());
    assertEquals(WORK_PERIOD, workExperienceEntity.getWorkPeriod());
    assertNotNull(workExperienceEntity.getId());
    assertNotNull(workExperienceEntity.getCreatedAt());
  }
}
