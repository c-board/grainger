package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "price_recommendations")
public class PriceRecommendation {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String sku;

  @Column(name = "customer_id", nullable = false)
  private String customerId;

  @Column(name = "current_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal currentPrice;

  @Column(name = "recommended_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal recommendedPrice;

  @Column(name = "floor_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal floorPrice;

  @Column(name = "competitor_price", precision = 12, scale = 2)
  private BigDecimal competitorPrice;

  @Column(name = "delta_pct", nullable = false, precision = 8, scale = 4)
  private BigDecimal deltaPct;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RecommendationStatus status;

  @Column(nullable = false)
  private String rationale;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "resolved_at")
  private Instant resolvedAt;

  protected PriceRecommendation() {
  }

  public PriceRecommendation(
      UUID id,
      String sku,
      String customerId,
      BigDecimal currentPrice,
      BigDecimal recommendedPrice,
      BigDecimal floorPrice,
      BigDecimal competitorPrice,
      BigDecimal deltaPct,
      RecommendationStatus status,
      String rationale,
      Instant createdAt
  ) {
    this.id = id;
    this.sku = sku;
    this.customerId = customerId;
    this.currentPrice = currentPrice;
    this.recommendedPrice = recommendedPrice;
    this.floorPrice = floorPrice;
    this.competitorPrice = competitorPrice;
    this.deltaPct = deltaPct;
    this.status = status;
    this.rationale = rationale;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public String getSku() {
    return sku;
  }

  public String getCustomerId() {
    return customerId;
  }

  public BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public BigDecimal getRecommendedPrice() {
    return recommendedPrice;
  }

  public BigDecimal getFloorPrice() {
    return floorPrice;
  }

  public BigDecimal getCompetitorPrice() {
    return competitorPrice;
  }

  public BigDecimal getDeltaPct() {
    return deltaPct;
  }

  public RecommendationStatus getStatus() {
    return status;
  }

  public String getRationale() {
    return rationale;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getResolvedAt() {
    return resolvedAt;
  }

  public void approve(Instant when) {
    this.status = RecommendationStatus.APPROVED;
    this.resolvedAt = when;
  }

  public void reject(Instant when) {
    this.status = RecommendationStatus.REJECTED;
    this.resolvedAt = when;
  }

  public void supersede(Instant when) {
    this.status = RecommendationStatus.SUPERSEDED;
    this.resolvedAt = when;
  }
}
