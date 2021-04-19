package org.example.protic.domain.workexperience;

import org.example.protic.commons.ValidationException;
import org.example.protic.domain.ValueObject;

import java.util.Objects;

public abstract class RestrictedField<S> implements ValueObject {

  private final S value;

  private RestrictedField(S value) {
    if (Objects.isNull(value)) {
      throw new ValidationException("Null work experience field value.");
    }
    this.value = value;
  }

  public S getValue() {
    return value;
  }

  public abstract boolean isPublic();

  private static final class PrivateField<T> extends RestrictedField<T> {
    private PrivateField(T value) {
      super(value);
    }

    @Override
    public boolean isPublic() {
      return false;
    }
  }

  private static final class PublicField<T> extends RestrictedField<T> {
    private PublicField(T value) {
      super(value);
    }

    @Override
    public boolean isPublic() {
      return true;
    }
  }

  public static <T> PrivateField<T> ofPrivate(T value) {
    return new PrivateField<>(value);
  }

  public static <T> PublicField<T> ofPublic(T value) {
    return new PublicField<>(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestrictedField<?> that = (RestrictedField<?>) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
