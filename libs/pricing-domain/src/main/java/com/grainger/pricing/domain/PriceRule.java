package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "price_rules")
public class PriceRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String category;

  @Column(name = "min_margin", nullable = false, precision = 6, scale = 4)
  private BigDecimal minMargin;

  @Column(name = "competitor_undercut_threshold", nullable = false, precision = 6, scale = 4)
  private BigDecimal competitorUndercutThreshold;

  protected PriceRule() {
  }

  public Long getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public BigDecimal getMinMargin() {
    return minMargin;
  }

  public BigDecimal getCompetitorUndercutThreshold() {
    return competitorUndercutThreshold;
  }
}
