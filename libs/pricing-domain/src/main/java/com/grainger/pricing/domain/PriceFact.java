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
@Table(name = "price_facts", schema = "analytics")
public class PriceFact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String sku;

  @Column(name = "customer_segment")
  private String customerSegment;

  @Column(name = "list_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal listPrice;

  @Column(name = "recommended_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal recommendedPrice;

  @Column(name = "competitor_price", precision = 12, scale = 2)
  private BigDecimal competitorPrice;

  @Column(name = "floor_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal floorPrice;

  @Column(nullable = false)
  private String status;

  @Column(name = "recorded_at", nullable = false)
  private Instant recordedAt;

  protected PriceFact() {
  }

  public PriceFact(
      String sku,
      String customerSegment,
      BigDecimal listPrice,
      BigDecimal recommendedPrice,
      BigDecimal competitorPrice,
      BigDecimal floorPrice,
      String status,
      Instant recordedAt
  ) {
    this.sku = sku;
    this.customerSegment = customerSegment;
    this.listPrice = listPrice;
    this.recommendedPrice = recommendedPrice;
    this.competitorPrice = competitorPrice;
    this.floorPrice = floorPrice;
    this.status = status;
    this.recordedAt = recordedAt;
  }

  public Long getId() {
    return id;
  }
}
