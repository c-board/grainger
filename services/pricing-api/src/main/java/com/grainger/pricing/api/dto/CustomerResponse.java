package com.grainger.pricing.api.dto;

import com.grainger.pricing.core.CustomerSegment;
import java.math.BigDecimal;

public record CustomerResponse(
    String id,
    String name,
    CustomerSegment segment,
    BigDecimal discount
) {
}
