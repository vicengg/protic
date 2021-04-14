package org.example.protic.domain;

import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

  @Test
  @DisplayName("It tries to create a user ID with null value.")
  void createUserIdWithNullValue() {
    ValidationException exception = assertThrows(ValidationException.class, () -> UserId.of(null));
    assertEquals("Null or empty user ID.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a user ID with empty value.")
  void createUserIdWithEmptyValue() {
    ValidationException exception = assertThrows(ValidationException.class, () -> UserId.of(""));
    assertEquals("Null or empty user ID.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a user ID from a valid value.")
  void createUserIdWithValidValue() {
    String value = "value";
    UserId userId = UserId.of(value);
    assertEquals(value, userId.getValue());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    UserId userId1 = UserId.of("user_id");
    UserId userId2 = UserId.of("user_id");
    assertEquals(userId1, userId1);
    assertNotEquals(userId1, new Object());
    assertEquals(userId1, userId2);
    assertEquals(userId1.hashCode(), userId2.hashCode());
  }
}
