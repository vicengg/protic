package org.example.protic.domain.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobTitleTest {

  @Test
  @DisplayName("It tries to create a job title with null name.")
  void createJobTitleWithNullName() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> JobTitle.of(null));
    assertEquals("Null or empty job title name.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a job title with empty name.")
  void createJobTitleWithEmptyName() {
    ValidationException exception = assertThrows(ValidationException.class, () -> JobTitle.of(""));
    assertEquals("Null or empty job title name.", exception.getMessage());
  }

  @Test
  @DisplayName(
      "It tries to create a job title with a name with more characters than maximum allowed.")
  void createJobTitleWithTooLongName() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () -> JobTitle.of(StringUtils.repeat("A", JobTitle.MAX_NAME_SIZE + 1)));
    assertEquals("Job title name cannot be greater than 50 characters.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a job title from a valid name.")
  void createJobTitleWithValidName() {
    String name = "job title";
    JobTitle jobTitle = JobTitle.of(name);
    assertEquals(name.toUpperCase(), jobTitle.getName());
  }

  @Test
  @DisplayName("It creates a job title from a name containing multiple space in a row.")
  void createJobTitleWithNameContainingMultipleSpaces() {
    String name = "job  title  with  multiple  spaces";
    JobTitle jobTitle = JobTitle.of(name);
    assertEquals(name.trim().toUpperCase(), jobTitle.getName());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    JobTitle jobTitle1 = JobTitle.of("job title");
    JobTitle jobTitle2 = JobTitle.of("job title");
    assertEquals(jobTitle1, jobTitle1);
    assertNotEquals(jobTitle1, new Object());
    assertEquals(jobTitle1, jobTitle2);
    assertEquals(jobTitle1.hashCode(), jobTitle2.hashCode());
  }
}
