package com.grainger.pricing.core;

import java.math.BigDecimal;

public record Quote(
    BigDecimal floor,
    BigDecimal segmentPrice,
    boolean matchedCompetitor,
    BigDecimal recommended,
    BigDecimal deltaPct,
    boolean autoApply,
    String rationale
) {
}
