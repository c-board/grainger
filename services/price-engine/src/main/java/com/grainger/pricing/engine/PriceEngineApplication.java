package com.grainger.pricing.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.grainger.pricing")
@EntityScan("com.grainger.pricing.domain")
@EnableJpaRepositories("com.grainger.pricing.domain.repo")
public class PriceEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(PriceEngineApplication.class, args);
  }
}
