package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.PriceRecommendation;
import com.grainger.pricing.domain.RecommendationStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRecommendationRepository extends JpaRepository<PriceRecommendation, UUID> {
  List<PriceRecommendation> findByStatusOrderByCreatedAtDesc(RecommendationStatus status);

  List<PriceRecommendation> findBySkuAndCustomerIdAndStatus(
      String sku,
      String customerId,
      RecommendationStatus status
  );

  long countByStatus(RecommendationStatus status);

  Optional<PriceRecommendation> findTopBySkuAndCustomerIdOrderByCreatedAtDesc(String sku, String customerId);
}
