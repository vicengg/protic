package org.example.protic.domain.workexperience;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

public final class WorkPeriod implements ValueObject {

  private final LocalDate startDate;
  private final LocalDate endDate;

  public WorkPeriod(LocalDate startDate, LocalDate endDate) {
    if (Objects.isNull(startDate)) {
      throw new ValidationException("Null period start date.");
    }
    if (startDate.isAfter(LocalDate.now())) {
      throw new ValidationException("The period start date cannot be a future date.");
    }
    this.startDate = startDate;
    Optional.ofNullable(endDate)
        .ifPresent(
            date -> {
              if (date.isAfter(LocalDate.now())) {
                throw new ValidationException("The period end date cannot be a future date.");
              }
              if (date.isBefore(startDate)) {
                throw new ValidationException(
                    "The period end date cannot be previous to the start date.");
              }
            });
    this.endDate = endDate;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public Optional<LocalDate> getEndDate() {
    return Optional.ofNullable(endDate);
  }

  public Period getDuration() {
    return Optional.ofNullable(endDate)
        .map(date -> Period.between(startDate, date))
        .orElse(Period.between(startDate, LocalDate.now()));
  }

  public static Builder from(LocalDate startDate) {
    return new Builder(startDate);
  }

  public static final class Builder {
    private final LocalDate startDate;

    private Builder(LocalDate startDate) {
      this.startDate = startDate;
    }

    public WorkPeriod to(LocalDate endDate) {
      return new WorkPeriod(startDate, endDate);
    }

    public WorkPeriod toPresent() {
      return new WorkPeriod(startDate, null);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkPeriod that = (WorkPeriod) o;
    return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate);
  }
}
