package com.makotodecor.config;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class I18nConfiguration {
  private static final String BASE_NAME = "i18n/messages";

  @Value("${i18n.cache-seconds}")
  private int cacheDurationSeconds;

  @Bean
  public AcceptHeaderLocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(Locale.forLanguageTag("vi"));
    return localeResolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames(BASE_NAME);
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setCacheSeconds(cacheDurationSeconds);
    return messageSource;
  }
}
