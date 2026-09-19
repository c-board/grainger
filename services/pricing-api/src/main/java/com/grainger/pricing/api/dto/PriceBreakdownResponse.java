package com.grainger.pricing.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record PriceBreakdownResponse(
    String sku,
    String productName,
    String customerId,
    String customerName,
    String segment,
    BigDecimal cost,
    BigDecimal minMargin,
    BigDecimal floor,
    BigDecimal listPrice,
    BigDecimal segmentDiscount,
    BigDecimal segmentPrice,
    String competitor,
    BigDecimal competitorPrice,
    BigDecimal undercutThreshold,
    BigDecimal currentPrice,
    String currentSource,
    BigDecimal recommendedPrice,
    boolean matchedCompetitor,
    String rationale,
    List<DerivationStep> steps
) {
}
