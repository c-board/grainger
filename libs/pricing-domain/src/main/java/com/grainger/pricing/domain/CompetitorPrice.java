package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "competitor_prices")
public class CompetitorPrice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sku;

  @Column(nullable = false)
  private String competitor;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(name = "observed_at", nullable = false)
  private Instant observedAt;

  protected CompetitorPrice() {
  }

  public CompetitorPrice(String sku, String competitor, BigDecimal amount, Instant observedAt) {
    this.sku = sku;
    this.competitor = competitor;
    this.amount = amount;
    this.observedAt = observedAt;
  }

  public Long getId() {
    return id;
  }

  public String getSku() {
    return sku;
  }

  public String getCompetitor() {
    return competitor;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Instant getObservedAt() {
    return observedAt;
  }
}
