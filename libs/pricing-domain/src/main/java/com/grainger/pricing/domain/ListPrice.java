package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "list_prices")
public class ListPrice {

  @Id
  private String sku;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected ListPrice() {
  }

  public String getSku() {
    return sku;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
