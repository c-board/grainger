package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "applied_prices")
public class AppliedPrice {

  @EmbeddedId
  private AppliedPriceId id;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(nullable = false)
  private String source;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected AppliedPrice() {
  }

  public AppliedPrice(AppliedPriceId id, BigDecimal amount, String source, Instant updatedAt) {
    this.id = id;
    this.amount = amount;
    this.source = source;
    this.updatedAt = updatedAt;
  }

  public AppliedPriceId getId() {
    return id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getSource() {
    return source;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void apply(BigDecimal nextAmount, String nextSource, Instant when) {
    this.amount = nextAmount;
    this.source = nextSource;
    this.updatedAt = when;
  }
}
