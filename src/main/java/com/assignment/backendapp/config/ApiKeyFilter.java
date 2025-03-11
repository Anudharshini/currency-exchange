package com.assignment.backendapp.config;

import com.assignment.backendapp.exceptions.AuthenticationFailureException;
import com.assignment.backendapp.exceptions.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

  @Value("${api.key}")
  private String validApiKey;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String apiKey = request.getHeader("X-API-KEY");

    if (apiKey == null || !apiKey.trim().equals(validApiKey.trim())) {

      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
      throw new AuthenticationFailureException(ErrorCode.AUTH_INVALID_API_KEY, "Invalid API key");
    }

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken("API_KEY_USER", null, Collections.emptyList());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
