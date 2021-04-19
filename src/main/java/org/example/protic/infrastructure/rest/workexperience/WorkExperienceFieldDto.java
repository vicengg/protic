package org.example.protic.infrastructure.rest.workexperience;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.protic.domain.workexperience.RestrictedField;

import java.util.function.Function;

public class WorkExperienceFieldDto<T> {

  @JsonProperty("public")
  public boolean isPublic;

  public T content;

  public static <S, T> WorkExperienceFieldDto<T> of(
          RestrictedField<S> restrictedField, Function<S, T> mappingFunction) {
    WorkExperienceFieldDto<T> dto = new WorkExperienceFieldDto<>();
    dto.isPublic = restrictedField.isPublic();
    dto.content = mappingFunction.apply(restrictedField.getValue());
    return dto;
  }
}
