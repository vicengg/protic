package org.example.protic.commons;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationExceptionTest {

  @Test
  @DisplayName("It tries to create a validation exception with a null message.")
  void createValidationExceptionWithNullMessage() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> new ValidationException(null));
    assertEquals("Null validation exception message.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a validation exception with a valid message.")
  void createValidationExceptionWithValidMessage() {
    String message = "message";
    ValidationException validationException = new ValidationException(message);
    assertEquals(message, validationException.getMessage());
  }
}
