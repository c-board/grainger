package com.grainger.pricing.events;

import java.math.BigDecimal;
import java.time.Instant;

public record CompetitorPriceObserved(
    String sku,
    String competitor,
    BigDecimal amount,
    Instant observedAt
) {
}
