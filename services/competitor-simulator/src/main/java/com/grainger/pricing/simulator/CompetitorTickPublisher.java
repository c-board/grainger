package com.grainger.pricing.simulator;

import com.grainger.pricing.domain.ListPrice;
import com.grainger.pricing.domain.repo.ListPriceRepository;
import com.grainger.pricing.events.CompetitorPriceObserved;
import com.grainger.pricing.events.Topics;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompetitorTickPublisher {

  private static final Logger log = LoggerFactory.getLogger(CompetitorTickPublisher.class);
  private static final List<String> COMPETITORS = List.of(
      "Fastenal",
      "MSC Industrial",
      "Amazon Business",
      "Uline"
  );

  private final ListPriceRepository listPrices;
  private final KafkaTemplate<String, Object> kafka;

  public CompetitorTickPublisher(
      ListPriceRepository listPrices,
      KafkaTemplate<String, Object> kafka
  ) {
    this.listPrices = listPrices;
    this.kafka = kafka;
  }

  @Scheduled(fixedDelayString = "${simulator.interval-ms:8000}")
  public void publishTick() {
    List<ListPrice> catalog = listPrices.findAll();
    if (catalog.isEmpty()) {
      return;
    }
    ThreadLocalRandom random = ThreadLocalRandom.current();
    ListPrice list = catalog.get(random.nextInt(catalog.size()));
    String competitor = COMPETITORS.get(random.nextInt(COMPETITORS.size()));
    double factor = 0.86 + random.nextDouble() * 0.16;
    BigDecimal amount = list.getAmount()
        .multiply(BigDecimal.valueOf(factor))
        .setScale(2, RoundingMode.HALF_UP);
    CompetitorPriceObserved event = new CompetitorPriceObserved(
        list.getSku(),
        competitor,
        amount,
        Instant.now()
    );
    kafka.send(Topics.COMPETITOR_PRICES, event.sku(), event);
    log.info("Published {} {} @ {}", competitor, event.sku(), amount);
  }
}
