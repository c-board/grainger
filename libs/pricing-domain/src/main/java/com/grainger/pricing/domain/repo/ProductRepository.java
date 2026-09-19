package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
