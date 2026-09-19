package com.grainger.pricing.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PriceRecommendationCreated(
    UUID recommendationId,
    String sku,
    String customerId,
    BigDecimal currentPrice,
    BigDecimal recommendedPrice,
    BigDecimal floorPrice,
    BigDecimal competitorPrice,
    BigDecimal deltaPct,
    String status,
    String rationale,
    Instant createdAt
) {
}
