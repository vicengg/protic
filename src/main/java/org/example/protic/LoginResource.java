package org.example.protic;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class LoginResource {

  public LoginResource() {}

  @RequestMapping(
      method = RequestMethod.GET,
      value = "/login",
      produces = MediaType.TEXT_HTML_VALUE)
  public @ResponseBody String login() {
    return "Log in with <a href=\"/oauth2/authorization/github\">GitHub</a>";
  }

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public @ResponseBody String home() {
    DefaultOAuth2User user = getUser();
    return "Hello " + user.getAttribute("login");
  }

  private static DefaultOAuth2User getUser() {
    return (DefaultOAuth2User)
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
