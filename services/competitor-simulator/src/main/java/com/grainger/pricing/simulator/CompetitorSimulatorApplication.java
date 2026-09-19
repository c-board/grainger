package com.grainger.pricing.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.grainger.pricing.simulator")
@EntityScan("com.grainger.pricing.domain")
@EnableJpaRepositories("com.grainger.pricing.domain.repo")
@EnableScheduling
public class CompetitorSimulatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(CompetitorSimulatorApplication.class, args);
  }
}
