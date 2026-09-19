package com.grainger.pricing.api.dto;

import java.math.BigDecimal;

public record ProductResponse(
    String sku,
    String name,
    String category,
    String unit,
    BigDecimal cost,
    BigDecimal listPrice,
    BigDecimal competitorPrice,
    String competitor,
    BigDecimal recommendedPrice,
    String recommendationStatus
) {
}
