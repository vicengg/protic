package org.example.protic;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/oauth2/authorization/github")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .loginPage("/oauth2/authorization/github");
  }
}
