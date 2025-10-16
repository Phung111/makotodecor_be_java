package com.makotodecor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ConfigurationPropertiesScan
@ComponentScan(basePackages = { "com.makotodecor" })
public class MakotodecorApplication {

  public static void main(String[] args) {
    SpringApplication.run(MakotodecorApplication.class, args);
  }

}
