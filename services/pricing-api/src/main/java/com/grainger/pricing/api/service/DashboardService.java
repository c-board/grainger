package com.grainger.pricing.api.service;

import com.grainger.pricing.api.dto.DashboardResponse;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.RecommendationStatus;
import com.grainger.pricing.domain.repo.CompetitorPriceRepository;
import com.grainger.pricing.domain.repo.PriceRecommendationRepository;
import com.grainger.pricing.domain.repo.ProductRepository;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

  private final PriceRecommendationRepository recommendations;
  private final CompetitorPriceRepository competitorPrices;
  private final ProductRepository products;

  public DashboardService(
      PriceRecommendationRepository recommendations,
      CompetitorPriceRepository competitorPrices,
      ProductRepository products
  ) {
    this.recommendations = recommendations;
    this.competitorPrices = competitorPrices;
    this.products = products;
  }

  @Transactional(readOnly = true)
  public DashboardResponse dashboard() {
    Map<String, String> names = products.findAll().stream()
        .collect(Collectors.toMap(Product::getSku, Product::getName));
    var moves = competitorPrices.findTop20ByOrderByObservedAtDesc().stream()
        .map(price -> new DashboardResponse.CompetitorMove(
            price.getSku(),
            names.getOrDefault(price.getSku(), price.getSku()),
            price.getCompetitor(),
            price.getAmount(),
            price.getObservedAt()
        ))
        .toList();
    return new DashboardResponse(
        recommendations.countByStatus(RecommendationStatus.PENDING_APPROVAL),
        recommendations.countByStatus(RecommendationStatus.AUTO_APPLIED),
        recommendations.countByStatus(RecommendationStatus.APPROVED),
        recommendations.countByStatus(RecommendationStatus.REJECTED),
        moves
    );
  }
}
