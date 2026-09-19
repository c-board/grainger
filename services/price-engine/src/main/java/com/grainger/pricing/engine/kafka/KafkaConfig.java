package com.grainger.pricing.engine.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grainger.pricing.events.Topics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaConfig {

  @Bean(name = "kafkaObjectMapper")
  public ObjectMapper kafkaObjectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }

  @Bean
  public ProducerFactory<String, Object> producerFactory(
      KafkaProperties properties,
      @Qualifier("kafkaObjectMapper") ObjectMapper kafkaObjectMapper
  ) {
    JsonSerializer<Object> serializer = new JsonSerializer<>(kafkaObjectMapper);
    serializer.setAddTypeInfo(false);
    return new DefaultKafkaProducerFactory<>(
        properties.buildProducerProperties(null),
        new StringSerializer(),
        serializer
    );
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic competitorPricesTopic() {
    return new NewTopic(Topics.COMPETITOR_PRICES, 1, (short) 1);
  }

  @Bean
  public NewTopic priceRecommendationsTopic() {
    return new NewTopic(Topics.PRICE_RECOMMENDATIONS, 1, (short) 1);
  }
}
