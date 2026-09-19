package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.PriceFact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceFactRepository extends JpaRepository<PriceFact, Long> {
}
