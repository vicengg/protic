package org.example.protic.infrastructure.connector;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubUserDto {

  @JsonProperty("id")
  public String id;

  @JsonProperty("login")
  public String login;

  @JsonProperty("avatar_url")
  public String avatarUrl;
}
