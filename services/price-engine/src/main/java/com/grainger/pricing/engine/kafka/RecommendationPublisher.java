package com.grainger.pricing.engine.kafka;

import com.grainger.pricing.events.PriceRecommendationCreated;
import com.grainger.pricing.events.Topics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class RecommendationPublisher {

  private final KafkaTemplate<String, Object> kafka;

  public RecommendationPublisher(KafkaTemplate<String, Object> kafka) {
    this.kafka = kafka;
  }

  public void publish(PriceRecommendationCreated event) {
    kafka.send(Topics.PRICE_RECOMMENDATIONS, event.sku(), event);
  }
}
