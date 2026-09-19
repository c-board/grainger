package com.grainger.pricing.domain.repo;

import com.grainger.pricing.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
