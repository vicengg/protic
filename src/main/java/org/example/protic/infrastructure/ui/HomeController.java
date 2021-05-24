package org.example.protic.infrastructure.ui;

import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.rest.RestControllerUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HomeController {

  public HomeController() {}

  @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  public String home(Model model) {
    User user = RestControllerUtils.getUser();
    model.addAttribute("user", user);
    return "index";
  }
}
