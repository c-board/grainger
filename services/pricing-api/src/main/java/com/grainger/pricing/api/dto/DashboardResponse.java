package com.grainger.pricing.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record DashboardResponse(
    long pendingApprovals,
    long autoApplied,
    long approved,
    long rejected,
    List<CompetitorMove> recentCompetitorMoves
) {
  public record CompetitorMove(
      String sku,
      String productName,
      String competitor,
      BigDecimal amount,
      Instant observedAt
  ) {
  }
}
