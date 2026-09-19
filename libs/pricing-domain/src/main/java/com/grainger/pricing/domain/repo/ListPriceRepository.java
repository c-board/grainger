package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.ListPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListPriceRepository extends JpaRepository<ListPrice, String> {
}
