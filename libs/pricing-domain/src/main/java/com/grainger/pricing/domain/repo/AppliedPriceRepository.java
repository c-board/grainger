package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.AppliedPrice;
import com.grainger.pricing.domain.AppliedPriceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedPriceRepository extends JpaRepository<AppliedPrice, AppliedPriceId> {
}
