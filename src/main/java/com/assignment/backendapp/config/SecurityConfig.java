package com.assignment.backendapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig provides configuration which allows http calls which can be accessed by
 * application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final ApiKeyFilter apiKeyFilter;

  public SecurityConfig(ApiKeyFilter apiKeyFilter) {
    this.apiKeyFilter = apiKeyFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf()
        .disable()
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                    .permitAll()
                    .requestMatchers("/api/calculate")
                    .authenticated()
                    .anyRequest()
                    .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
