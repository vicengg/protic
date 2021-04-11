package org.example.protic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class ProticApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProticApplication.class, args);
  }
}
