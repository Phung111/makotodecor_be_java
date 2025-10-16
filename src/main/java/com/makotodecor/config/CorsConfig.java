package com.makotodecor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsConfig {

  private List<String> allowedOrigins;
  private List<String> allowedMethods;
  private List<String> allowedHeaders;
  private boolean allowCredentials;
  private long maxAge;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
      configuration.setAllowedOrigins(allowedOrigins);
    } else {
      configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    }

    if (allowedMethods != null && !allowedMethods.isEmpty()) {
      configuration.setAllowedMethods(allowedMethods);
    } else {
      configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    }

    if (allowedHeaders != null && !allowedHeaders.isEmpty()) {
      configuration.setAllowedHeaders(allowedHeaders);
    } else {
      configuration.setAllowedHeaders(Arrays.asList("*"));
    }

    configuration.setAllowCredentials(allowCredentials);
    configuration.setMaxAge(maxAge > 0 ? maxAge : 3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
