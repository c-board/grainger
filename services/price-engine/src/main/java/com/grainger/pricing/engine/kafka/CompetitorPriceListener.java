package com.grainger.pricing.engine.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grainger.pricing.engine.service.PriceRecalculationService;
import com.grainger.pricing.events.CompetitorPriceObserved;
import com.grainger.pricing.events.Topics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CompetitorPriceListener {

  private final PriceRecalculationService recalculation;
  private final ObjectMapper kafkaObjectMapper;

  public CompetitorPriceListener(
      PriceRecalculationService recalculation,
      @Qualifier("kafkaObjectMapper") ObjectMapper kafkaObjectMapper
  ) {
    this.recalculation = recalculation;
    this.kafkaObjectMapper = kafkaObjectMapper;
  }

  @KafkaListener(topics = Topics.COMPETITOR_PRICES)
  public void onMessage(String payload) throws Exception {
    recalculation.handle(kafkaObjectMapper.readValue(payload, CompetitorPriceObserved.class));
  }
}
