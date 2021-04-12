package org.example.protic;

import org.example.protic.infrastructure.rest.workexperience.WorkExperienceResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfig extends ResourceConfig {

  public RestConfig() {
    register(LoginResource.class);
    register(WorkExperienceResource.class);
  }
}
