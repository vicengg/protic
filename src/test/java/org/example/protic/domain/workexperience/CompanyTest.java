package org.example.protic.domain.workexperience;

import org.apache.commons.lang.StringUtils;
import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

  @Test
  @DisplayName("It tries to create a company with null name.")
  void createCompanyWithNullName() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> Company.of(null));
    assertEquals("Null or empty company name.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a company with empty name.")
  void createCompanyWithEmptyName() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> Company.of(""));
    assertEquals("Null or empty company name.", exception.getMessage());
  }

  @Test
  @DisplayName(
      "It tries to create a company with a name with more characters than maximum allowed.")
  void createCompanyWithTooLongName() {
    ValidationException exception =
        assertThrows(
            ValidationException.class,
            () -> Company.of(StringUtils.repeat("A", Company.MAX_NAME_SIZE + 1)));
    assertEquals("Company name cannot be greater than 50 characters.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a company from a valid name.")
  void createCompanyWithValidName() {
    String name = "company";
    Company company = Company.of(name);
    assertEquals(name.toUpperCase(), company.getName());
  }

  @Test
  @DisplayName("It creates a company from a name containing multiple space in a row.")
  void createCompanyWithNameContainingMultipleSpaces() {
    String name = "company  with  multiple  spaces";
    Company company = Company.of(name);
    assertEquals(name.trim().toUpperCase(), company.getName());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    Company company1 = Company.of("company");
    Company company2 = Company.of("company");
    assertEquals(company1, company1);
    assertNotEquals(company1, new Object());
    assertEquals(company1, company2);
    assertEquals(company1.hashCode(), company2.hashCode());
  }
}
