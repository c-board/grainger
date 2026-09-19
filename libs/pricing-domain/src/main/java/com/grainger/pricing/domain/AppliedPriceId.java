package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AppliedPriceId implements Serializable {

  @Column(name = "sku", nullable = false)
  private String sku;

  @Column(name = "customer_id", nullable = false)
  private String customerId;

  protected AppliedPriceId() {
  }

  public AppliedPriceId(String sku, String customerId) {
    this.sku = sku;
    this.customerId = customerId;
  }

  public String getSku() {
    return sku;
  }

  public String getCustomerId() {
    return customerId;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AppliedPriceId that)) {
      return false;
    }
    return Objects.equals(sku, that.sku) && Objects.equals(customerId, that.customerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sku, customerId);
  }
}
