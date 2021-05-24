package org.example.protic.infrastructure.rest;

import org.example.protic.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Objects;
import java.util.Optional;

public class RestControllerUtils {

  private RestControllerUtils() {}

  public static ResponseEntity<RestDto> toOkResponse(RestDto dto) {
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  public static ResponseEntity<RestDto> toOkResponse(Void ignore) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private static DefaultOAuth2User getOAuthUser() {
    return (DefaultOAuth2User)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static User getUser() {
    return User.of(
        Objects.requireNonNull(getOAuthUser().getAttribute("id")).toString(),
        Objects.requireNonNull(getOAuthUser().getAttribute("login")).toString(),
        Optional.ofNullable(getOAuthUser().getAttribute("avatar_url"))
            .map(Object::toString)
            .orElse(null));
  }
}
