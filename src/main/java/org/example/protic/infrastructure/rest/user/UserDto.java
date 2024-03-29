package org.example.protic.infrastructure.rest.user;

import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.rest.RestDto;

public class UserDto implements RestDto {

  public String id;
  public String login;
  public String avatarUrl;

  public static UserDto of(User user) {
    UserDto userDto = new UserDto();
    userDto.id = user.getId();
    userDto.login = user.getLogin();
    userDto.avatarUrl = user.getAvatarUrl().orElse(null);
    return userDto;
  }
}
