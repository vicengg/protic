package org.example.protic.infrastructure.rest.user;

import org.example.protic.infrastructure.rest.RestControllerUtils;
import org.example.protic.infrastructure.rest.RestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestDto> getUser() {
    return RestControllerUtils.toOkResponse(UserDto.of(RestControllerUtils.getUser()));
  }

  @RequestMapping(
      value = "/logout",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> logout(HttpSession session) {
    session.invalidate();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
