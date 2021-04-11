package org.example.protic;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class RestConfig extends ResourceConfig {

  public RestConfig() {
    register(LoginResource.class);
  }
}
