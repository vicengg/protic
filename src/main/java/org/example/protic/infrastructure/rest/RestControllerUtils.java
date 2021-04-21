package org.example.protic.infrastructure.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class RestControllerUtils {

  private RestControllerUtils() {}

  public static ResponseEntity<RestDto> toOkResponse(RestDto dto) {
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  public static ResponseEntity<RestDto> toOkResponse(Void ignore) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public static DefaultOAuth2User getUser() {
    return (DefaultOAuth2User)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
