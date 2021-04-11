package org.example.protic.domain.experience;

import org.example.protic.commons.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class WorkPeriodTest {

  @Test
  @DisplayName("It tries to create a period with a null start date.")
  void createPeriodWithNullStartDate() {
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkPeriod.from(null).toPresent());
    assertEquals("Null period start date.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a period with a future start date.")
  void createPeriodWithFutureStartDate() {
    LocalDate startDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkPeriod.from(startDate).toPresent());
    assertEquals("The period start date cannot be a future date.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a period with a future end date.")
  void createPeriodWithFutureEndDate() {
    LocalDate startDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
    LocalDate endDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkPeriod.from(startDate).to(endDate));
    assertEquals("The period end date cannot be a future date.", exception.getMessage());
  }

  @Test
  @DisplayName("It tries to create a period with an end date before the start date.")
  void createPeriodWithAnEndDateBeforeStartDate() {
    LocalDate startDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
    LocalDate endDate = startDate.minus(1, ChronoUnit.DAYS);
    ValidationException exception =
        assertThrows(ValidationException.class, () -> WorkPeriod.from(startDate).to(endDate));
    assertEquals(
        "The period end date cannot be previous to the start date.", exception.getMessage());
  }

  @Test
  @DisplayName("It creates a period with end date.")
  void createPeriodWithEndDate() {
    LocalDate startDate = LocalDate.now().minus(2, ChronoUnit.DAYS);
    LocalDate endDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
    WorkPeriod workPeriod = WorkPeriod.from(startDate).to(endDate);
    assertEquals(startDate, workPeriod.getStartDate());
    assertTrue(workPeriod.getEndDate().isPresent());
    assertEquals(endDate, workPeriod.getEndDate().get());
    assertEquals(Period.between(startDate, endDate), workPeriod.getDuration());
  }

  @Test
  @DisplayName("It creates a period without end date.")
  void createPeriodWithoutEndDate() {
    LocalDate startDate = LocalDate.now().minus(2, ChronoUnit.DAYS);
    WorkPeriod workPeriod = WorkPeriod.from(startDate).toPresent();
    assertEquals(startDate, workPeriod.getStartDate());
    assertFalse(workPeriod.getEndDate().isPresent());
    assertEquals(Period.between(startDate, LocalDate.now()), workPeriod.getDuration());
  }

  @Test
  @DisplayName("Check equals and hashCode.")
  void checkEqualsAndHashCode() {
    WorkPeriod workPeriod1 = WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent();
    WorkPeriod workPeriod2 = WorkPeriod.from(LocalDate.now().minus(1, ChronoUnit.DAYS)).toPresent();
    assertEquals(workPeriod1, workPeriod1);
    assertNotEquals(workPeriod1, new Object());
    assertEquals(workPeriod1, workPeriod2);
    assertEquals(workPeriod1.hashCode(), workPeriod2.hashCode());
  }
}
