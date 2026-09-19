package com.grainger.pricing.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.grainger.pricing")
@EntityScan("com.grainger.pricing.domain")
@EnableJpaRepositories("com.grainger.pricing.domain.repo")
public class PricingApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(PricingApiApplication.class, args);
  }
}
