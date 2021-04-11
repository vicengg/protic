package org.example.protic.domain.experience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TechnologyTest {

  @Test
  @DisplayName("It tries to create a technology with null name.")
  void createTechnologyWithNullName() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> Technology.of(null));
    assertEquals("Null or empty technology name.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a technology with empty name.")
  void createTechnologyWithEmptyName() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> Technology.of(""));
    assertEquals("Null or empty technology name.", exception.getMessage());
  }

  @Test
  @DisplayName(
      "It tries to create a technology with a name with more characters than maximum allowed.")
  void createTechnologyWithTooLongName() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () -> Technology.of(StringUtils.repeat("A", Technology.MAX_NAME_SIZE + 1)));
    assertEquals("Technology name cannot be greater than 50 characters.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a technology from a valid name.")
  void createTechnologyWithValidName() {
    String name = "technology";
    Technology technology = Technology.of(name);
    assertEquals(name, technology.getName());
  }

  @Test
  @DisplayName("It creates a technology from a name containing multiple space in a row.")
  void createTechnologyWithNameContainingMultipleSpaces() {
    String name = "technology  with  multiple  spaces";
    Technology technology = Technology.of(name);
    assertEquals(name.trim(), technology.getName());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    Technology technology1 = Technology.of("technology");
    Technology technology2 = Technology.of("technology");
    assertEquals(technology1, technology1);
    assertNotEquals(technology1, new Object());
    assertEquals(technology1, technology2);
    assertEquals(technology1.hashCode(), technology2.hashCode());
  }
}
