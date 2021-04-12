package org.example.protic.infrastructure.rest.workexperience;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WorkExperienceFieldDto<T> {

  @JsonProperty("public")
  public boolean isPublic;

  public T content;
}
