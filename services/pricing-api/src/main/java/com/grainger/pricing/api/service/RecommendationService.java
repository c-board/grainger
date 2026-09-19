package com.grainger.pricing.api.service;

import com.grainger.pricing.api.dto.RecommendationResponse;
import com.grainger.pricing.domain.AppliedPrice;
import com.grainger.pricing.domain.AppliedPriceId;
import com.grainger.pricing.domain.Customer;
import com.grainger.pricing.domain.PriceRecommendation;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.RecommendationStatus;
import com.grainger.pricing.domain.repo.AppliedPriceRepository;
import com.grainger.pricing.domain.repo.CustomerRepository;
import com.grainger.pricing.domain.repo.PriceRecommendationRepository;
import com.grainger.pricing.domain.repo.ProductRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecommendationService {

  private final PriceRecommendationRepository recommendations;
  private final ProductRepository products;
  private final CustomerRepository customers;
  private final AppliedPriceRepository appliedPrices;

  public RecommendationService(
      PriceRecommendationRepository recommendations,
      ProductRepository products,
      CustomerRepository customers,
      AppliedPriceRepository appliedPrices
  ) {
    this.recommendations = recommendations;
    this.products = products;
    this.customers = customers;
    this.appliedPrices = appliedPrices;
  }

  @Transactional(readOnly = true)
  public List<RecommendationResponse> list(RecommendationStatus status) {
    return recommendations.findByStatusOrderByCreatedAtDesc(status).stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional
  public RecommendationResponse approve(UUID id) {
    PriceRecommendation recommendation = requirePending(id);
    Instant now = Instant.now();
    recommendation.approve(now);
    apply(recommendation, "APPROVED", now);
    return toResponse(recommendations.save(recommendation));
  }

  @Transactional
  public RecommendationResponse reject(UUID id) {
    PriceRecommendation recommendation = requirePending(id);
    recommendation.reject(Instant.now());
    return toResponse(recommendations.save(recommendation));
  }

  private PriceRecommendation requirePending(UUID id) {
    PriceRecommendation recommendation = recommendations.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown recommendation"));
    if (recommendation.getStatus() != RecommendationStatus.PENDING_APPROVAL) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Recommendation is not pending");
    }
    return recommendation;
  }

  private void apply(PriceRecommendation recommendation, String source, Instant when) {
    AppliedPriceId key = new AppliedPriceId(recommendation.getSku(), recommendation.getCustomerId());
    AppliedPrice applied = appliedPrices.findById(key)
        .orElseGet(() -> new AppliedPrice(key, recommendation.getRecommendedPrice(), source, when));
    applied.apply(recommendation.getRecommendedPrice(), source, when);
    appliedPrices.save(applied);
  }

  private RecommendationResponse toResponse(PriceRecommendation recommendation) {
    Product product = products.findById(recommendation.getSku()).orElseThrow();
    Customer customer = customers.findById(recommendation.getCustomerId()).orElseThrow();
    return new RecommendationResponse(
        recommendation.getId(),
        recommendation.getSku(),
        product.getName(),
        recommendation.getCustomerId(),
        customer.getName(),
        recommendation.getCurrentPrice(),
        recommendation.getRecommendedPrice(),
        recommendation.getFloorPrice(),
        recommendation.getCompetitorPrice(),
        recommendation.getDeltaPct(),
        recommendation.getStatus(),
        recommendation.getRationale(),
        recommendation.getCreatedAt()
    );
  }
}
