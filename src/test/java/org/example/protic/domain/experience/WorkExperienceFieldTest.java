package org.example.protic.domain.experience;

import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkExperienceFieldTest {

  @Test
  @DisplayName("It tries to create a private work experience field with null value.")
  void createPrivateWorkExperienceFieldWithNullValue() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkExperienceField.ofPrivate(null));
    assertEquals("Null work experience field value.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a public work experience field with null value.")
  void createPublicWorkExperienceFieldWithNullValue() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkExperienceField.ofPublic(null));
    assertEquals("Null work experience field value.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a private work experience field.")
  void createPrivateWorkExperienceField() {
    String value = "value";
    WorkExperienceField<String> workExperienceField = WorkExperienceField.ofPrivate(value);
    assertEquals(value, workExperienceField.getValue());
    assertFalse(workExperienceField.isPublic());
  }

  @Test
  @DisplayName("It creates a public work experience field.")
  void createPublicWorkExperienceField() {
    String value = "value";
    WorkExperienceField<String> workExperienceField = WorkExperienceField.ofPublic(value);
    assertEquals(value, workExperienceField.getValue());
    assertTrue(workExperienceField.isPublic());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    WorkExperienceField<String> privateWorkExperienceField1 =
        WorkExperienceField.ofPrivate("value");
    WorkExperienceField<String> privateWorkExperienceField2 =
        WorkExperienceField.ofPrivate("value");

    assertEquals(privateWorkExperienceField1, privateWorkExperienceField1);
    assertNotEquals(privateWorkExperienceField1, new Object());
    assertEquals(privateWorkExperienceField1, privateWorkExperienceField2);
    assertEquals(privateWorkExperienceField1.hashCode(), privateWorkExperienceField2.hashCode());

    WorkExperienceField<String> publicWorkExperienceField1 = WorkExperienceField.ofPublic("value");
    WorkExperienceField<String> publicWorkExperienceField2 = WorkExperienceField.ofPublic("value");

    assertEquals(publicWorkExperienceField1, publicWorkExperienceField1);
    assertNotEquals(publicWorkExperienceField1, new Object());
    assertEquals(publicWorkExperienceField1, publicWorkExperienceField2);
    assertEquals(publicWorkExperienceField1.hashCode(), publicWorkExperienceField2.hashCode());

    assertNotEquals(privateWorkExperienceField1, publicWorkExperienceField1);
  }
}
