package com.codingdevs.oms.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
public class SecurityConfig {

  @SuppressWarnings("deprecation")
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
      .httpBasic(withDefaults())
      .exceptionHandling(handling ->
        handling.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
      )
      .formLogin(login -> login.disable())
      .csrf(csrf -> csrf.disable());

    return http.build();
  }
}
