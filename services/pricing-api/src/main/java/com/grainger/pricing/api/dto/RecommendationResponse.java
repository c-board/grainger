package com.grainger.pricing.api.dto;

import com.grainger.pricing.domain.RecommendationStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RecommendationResponse(
    UUID id,
    String sku,
    String productName,
    String customerId,
    String customerName,
    BigDecimal currentPrice,
    BigDecimal recommendedPrice,
    BigDecimal floorPrice,
    BigDecimal competitorPrice,
    BigDecimal deltaPct,
    RecommendationStatus status,
    String rationale,
    Instant createdAt
) {
}
