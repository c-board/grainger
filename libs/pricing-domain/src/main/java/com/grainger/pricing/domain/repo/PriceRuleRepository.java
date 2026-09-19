package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.PriceRule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRuleRepository extends JpaRepository<PriceRule, Long> {
  Optional<PriceRule> findByCategory(String category);
}
