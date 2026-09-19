package com.grainger.pricing.core;

import java.math.BigDecimal;

public enum CustomerSegment {
  LIST(new BigDecimal("0.00")),
  CONTRACT(new BigDecimal("0.08")),
  STRATEGIC(new BigDecimal("0.15"));

  private final BigDecimal discount;

  CustomerSegment(BigDecimal discount) {
    this.discount = discount;
  }

  public BigDecimal discount() {
    return discount;
  }
}
