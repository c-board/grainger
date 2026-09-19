package com.grainger.pricing.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

  @Id
  private String sku;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String category;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal cost;

  @Column(nullable = false)
  private String unit;

  protected Product() {
  }

  public Product(String sku, String name, String category, BigDecimal cost, String unit) {
    this.sku = sku;
    this.name = name;
    this.category = category;
    this.cost = cost;
    this.unit = unit;
  }

  public String getSku() {
    return sku;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    return category;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public String getUnit() {
    return unit;
  }
}
