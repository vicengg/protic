package org.example.protic.domain.workexperience;

import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestrictedFieldTest {

  @Test
  @DisplayName("It tries to create a private work experience field with null value.")
  void createPrivateWorkExperienceFieldWithNullValue() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> RestrictedField.ofPrivate(null));
    assertEquals("Null work experience field value.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a public work experience field with null value.")
  void createPublicWorkExperienceFieldWithNullValue() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> RestrictedField.ofPublic(null));
    assertEquals("Null work experience field value.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a private work experience field.")
  void createPrivateWorkExperienceField() {
    String value = "value";
    RestrictedField<String> restrictedField = RestrictedField.ofPrivate(value);
    assertEquals(value, restrictedField.getValue());
    assertFalse(restrictedField.isPublic());
  }

  @Test
  @DisplayName("It creates a public work experience field.")
  void createPublicWorkExperienceField() {
    String value = "value";
    RestrictedField<String> restrictedField = RestrictedField.ofPublic(value);
    assertEquals(value, restrictedField.getValue());
    assertTrue(restrictedField.isPublic());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    RestrictedField<String> privateRestrictedField1 =
        RestrictedField.ofPrivate("value");
    RestrictedField<String> privateRestrictedField2 =
        RestrictedField.ofPrivate("value");

    assertEquals(privateRestrictedField1, privateRestrictedField1);
    assertNotEquals(privateRestrictedField1, new Object());
    assertEquals(privateRestrictedField1, privateRestrictedField2);
    assertEquals(privateRestrictedField1.hashCode(), privateRestrictedField2.hashCode());

    RestrictedField<String> publicRestrictedField1 = RestrictedField.ofPublic("value");
    RestrictedField<String> publicRestrictedField2 = RestrictedField.ofPublic("value");

    assertEquals(publicRestrictedField1, publicRestrictedField1);
    assertNotEquals(publicRestrictedField1, new Object());
    assertEquals(publicRestrictedField1, publicRestrictedField2);
    assertEquals(publicRestrictedField1.hashCode(), publicRestrictedField2.hashCode());

    assertNotEquals(privateRestrictedField1, publicRestrictedField1);
  }
}
