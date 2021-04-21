package org.example.protic.infrastructure.ui;

import org.example.protic.infrastructure.rest.RestControllerUtils;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.MessageFormat;
import java.util.Objects;

@Controller
@RequestMapping("/")
public class HomeController {

  public HomeController() {}

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String home(Model model) {
    DefaultOAuth2User user = RestControllerUtils.getUser();
    model.addAttribute(
        "message",
        MessageFormat.format(
            "Hola {0}", Objects.requireNonNull(user.getAttribute("login")).toString()));
    return "home";
  }
}
