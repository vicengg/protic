package org.example.protic.domain.user;

import org.example.protic.domain.ValueObject;

import java.util.Objects;
import java.util.Optional;

public class User implements ValueObject {

  private final String id;
  private final String login;
  private final String avatarUrl;

  private User(String id, String login, String avatarUrl) {
    this.id = Objects.requireNonNull(id, "Null user ID.");
    this.login = Objects.requireNonNull(login, "Null user login.");
    this.avatarUrl = avatarUrl;
  }

  public static User of(String id, String login, String avatarUrl) {
    return new User(id, login, avatarUrl);
  }

  public String getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public Optional<String> getAvatarUrl() {
    return Optional.ofNullable(avatarUrl);
  }

  public static User anonymous() {
    return new User(
        "0",
        "anónimo",
        "https://crysteland.com/wp-content/uploads/2016/12/unknown-user-460x460.png");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id)
        && Objects.equals(login, user.login)
        && Objects.equals(avatarUrl, user.avatarUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, login, avatarUrl);
  }
}
