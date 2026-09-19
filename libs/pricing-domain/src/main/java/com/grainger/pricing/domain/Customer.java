package com.grainger.pricing.domain;

import com.grainger.pricing.core.CustomerSegment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

  @Id
  private String id;

  @Column(nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CustomerSegment segment;

  protected Customer() {
  }

  public Customer(String id, String name, CustomerSegment segment) {
    this.id = id;
    this.name = name;
    this.segment = segment;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public CustomerSegment getSegment() {
    return segment;
  }
}
