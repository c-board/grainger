package com.grainger.pricing.core;

import java.math.BigDecimal;

public record QuoteRequest(
    BigDecimal cost,
    BigDecimal minMargin,
    BigDecimal listPrice,
    BigDecimal segmentDiscount,
    BigDecimal competitorPrice,
    BigDecimal undercutThreshold,
    BigDecimal currentPrice
) {
}
