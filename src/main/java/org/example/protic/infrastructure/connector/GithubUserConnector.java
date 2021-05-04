package org.example.protic.infrastructure.connector;

import org.example.protic.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Objects;

public class GithubUserConnector implements UserConnector {

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();
  private static final String URL = "https://api.github.com/user/{0}";

  @Override
  public User findUserById(String id) {
    ResponseEntity<GithubUserDto> responseEntity =
        REST_TEMPLATE.getForEntity(MessageFormat.format(URL, id), GithubUserDto.class);
    checkStatusCode(responseEntity);
    return mapToUser(
        Objects.requireNonNull(responseEntity.getBody(), "Null Github response body."));
  }

  private static User mapToUser(GithubUserDto githubUserDto) {
    return User.of(githubUserDto.id, githubUserDto.login, githubUserDto.avatarUrl);
  }

  private static void checkStatusCode(ResponseEntity<GithubUserDto> responseEntity) {
    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      // TODO: Change this.
      throw new RuntimeException("Error calling Github.");
    }
  }
}
