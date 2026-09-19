package com.grainger.pricing.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PricingCalculator {
  public static final RoundingMode MONEY = RoundingMode.HALF_UP;
  public static final int SCALE = 2;
  public static final BigDecimal AUTO_APPLY_THRESHOLD = new BigDecimal("0.05");

  private PricingCalculator() {
  }

  public static BigDecimal money(BigDecimal value) {
    return value.setScale(SCALE, MONEY);
  }

  public static Quote quote(QuoteRequest request) {
    BigDecimal floor = money(request.cost().multiply(BigDecimal.ONE.add(request.minMargin())));
    BigDecimal segmentPrice = money(request.listPrice().multiply(BigDecimal.ONE.subtract(request.segmentDiscount())));

    BigDecimal candidate = segmentPrice;
    boolean matchedCompetitor = false;
    if (request.competitorPrice() != null && request.competitorPrice().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal gap = segmentPrice.subtract(request.competitorPrice());
      if (gap.compareTo(BigDecimal.ZERO) > 0 && segmentPrice.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal gapPct = gap.divide(segmentPrice, 4, MONEY);
        if (gapPct.compareTo(request.undercutThreshold()) >= 0) {
          candidate = money(request.competitorPrice());
          matchedCompetitor = true;
        }
      }
    }

    BigDecimal recommended = floor.max(candidate);
    BigDecimal current = request.currentPrice();
    BigDecimal deltaPct = BigDecimal.ZERO;
    if (current.compareTo(BigDecimal.ZERO) > 0) {
      deltaPct = recommended.subtract(current).divide(current, 4, MONEY);
    }

    boolean autoApply = deltaPct.abs().compareTo(AUTO_APPLY_THRESHOLD) < 0;
    return new Quote(
        floor,
        segmentPrice,
        matchedCompetitor,
        recommended,
        deltaPct,
        autoApply,
        rationale(floor, segmentPrice, matchedCompetitor, recommended)
    );
  }

  private static String rationale(
      BigDecimal floor,
      BigDecimal segmentPrice,
      boolean matchedCompetitor,
      BigDecimal recommended
  ) {
    if (recommended.compareTo(floor) == 0 && floor.compareTo(segmentPrice) > 0) {
      return "Held at margin floor to protect cost.";
    }
    if (matchedCompetitor) {
      return "Matched competitor while remaining at or above the margin floor.";
    }
    return "Applied customer-segment discount off list price.";
  }
}
