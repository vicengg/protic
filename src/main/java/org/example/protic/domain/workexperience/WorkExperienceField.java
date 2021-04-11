package org.example.protic.domain.workexperience;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.util.Objects;

public abstract class WorkExperienceField<S> implements ValueObject {

  private final S value;

  private WorkExperienceField(S value) {
    if (Objects.isNull(value)) {
      throw new ValidationException("Null work experience field value.");
    }
    this.value = value;
  }

  public S getValue() {
    return value;
  }

  public abstract boolean isPublic();

  private static final class PrivateWorkExperienceField<T> extends WorkExperienceField<T> {
    private PrivateWorkExperienceField(T value) {
      super(value);
    }

    @Override
    public boolean isPublic() {
      return false;
    }
  }

  private static final class PublicWorkExperienceField<T> extends WorkExperienceField<T> {
    private PublicWorkExperienceField(T value) {
      super(value);
    }

    @Override
    public boolean isPublic() {
      return true;
    }
  }

  public static <T> PrivateWorkExperienceField<T> ofPrivate(T value) {
    return new PrivateWorkExperienceField<>(value);
  }

  public static <T> PublicWorkExperienceField<T> ofPublic(T value) {
    return new PublicWorkExperienceField<>(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorkExperienceField<?> that = (WorkExperienceField<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
