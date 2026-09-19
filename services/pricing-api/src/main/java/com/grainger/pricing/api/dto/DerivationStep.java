package com.grainger.pricing.api.dto;

import java.math.BigDecimal;

public record DerivationStep(
    String name,
    BigDecimal value,
    String note
) {
}
