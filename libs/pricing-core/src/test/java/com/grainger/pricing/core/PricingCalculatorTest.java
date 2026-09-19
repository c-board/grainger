package com.grainger.pricing.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingCalculatorTest {

  @Test
  void appliesSegmentDiscountOffList() {
    Quote quote = PricingCalculator.quote(new QuoteRequest(
        new BigDecimal("10.00"),
        new BigDecimal("0.20"),
        new BigDecimal("20.00"),
        CustomerSegment.CONTRACT.discount(),
        null,
        new BigDecimal("0.03"),
        new BigDecimal("18.40")
    ));

    assertEquals(new BigDecimal("12.00"), quote.floor());
    assertEquals(new BigDecimal("18.40"), quote.segmentPrice());
    assertEquals(new BigDecimal("18.40"), quote.recommended());
    assertFalse(quote.matchedCompetitor());
    assertTrue(quote.autoApply());
  }

  @Test
  void matchesCompetitorAboveFloor() {
    Quote quote = PricingCalculator.quote(new QuoteRequest(
        new BigDecimal("10.00"),
        new BigDecimal("0.20"),
        new BigDecimal("20.00"),
        CustomerSegment.LIST.discount(),
        new BigDecimal("17.00"),
        new BigDecimal("0.03"),
        new BigDecimal("20.00")
    ));

    assertTrue(quote.matchedCompetitor());
    assertEquals(new BigDecimal("17.00"), quote.recommended());
    assertEquals(new BigDecimal("-0.1500"), quote.deltaPct());
    assertFalse(quote.autoApply());
  }

  @Test
  void neverPricesBelowFloor() {
    Quote quote = PricingCalculator.quote(new QuoteRequest(
        new BigDecimal("10.00"),
        new BigDecimal("0.20"),
        new BigDecimal("20.00"),
        CustomerSegment.LIST.discount(),
        new BigDecimal("8.00"),
        new BigDecimal("0.03"),
        new BigDecimal("20.00")
    ));

    assertEquals(new BigDecimal("12.00"), quote.recommended());
    assertTrue(quote.matchedCompetitor());
  }
}
