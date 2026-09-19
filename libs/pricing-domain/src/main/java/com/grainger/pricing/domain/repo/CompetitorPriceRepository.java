package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.CompetitorPrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorPriceRepository extends JpaRepository<CompetitorPrice, Long> {
  Optional<CompetitorPrice> findFirstBySkuOrderByObservedAtDesc(String sku);

  List<CompetitorPrice> findTop20ByOrderByObservedAtDesc();
}
