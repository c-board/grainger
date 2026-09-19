package com.grainger.pricing.api.service;

import com.grainger.pricing.api.dto.CustomerResponse;
import com.grainger.pricing.api.dto.ProductResponse;
import com.grainger.pricing.core.Quote;
import com.grainger.pricing.domain.Customer;
import com.grainger.pricing.domain.ListPrice;
import com.grainger.pricing.domain.PriceRecommendation;
import com.grainger.pricing.domain.PriceRule;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.repo.CustomerRepository;
import com.grainger.pricing.domain.repo.PriceRecommendationRepository;
import com.grainger.pricing.domain.repo.ProductRepository;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CatalogService {

  static final String LIST_CUSTOMER_ID = "CUST-004";

  private final ProductRepository products;
  private final CustomerRepository customers;
  private final PriceRecommendationRepository recommendations;
  private final PricingContextService context;

  public CatalogService(
      ProductRepository products,
      CustomerRepository customers,
      PriceRecommendationRepository recommendations,
      PricingContextService context
  ) {
    this.products = products;
    this.customers = customers;
    this.recommendations = recommendations;
    this.context = context;
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> listProducts() {
    Customer listCustomer = customers.findById(LIST_CUSTOMER_ID)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Seed customer missing"));
    return products.findAll().stream()
        .sorted(Comparator.comparing(Product::getSku))
        .map(product -> toProduct(product, listCustomer))
        .toList();
  }

  @Transactional(readOnly = true)
  public ProductResponse getProduct(String sku) {
    Customer listCustomer = customers.findById(LIST_CUSTOMER_ID)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Seed customer missing"));
    return toProduct(context.requireProduct(sku), listCustomer);
  }

  @Transactional(readOnly = true)
  public List<CustomerResponse> listCustomers() {
    return customers.findAll().stream()
        .sorted(Comparator.comparing(Customer::getId))
        .map(customer -> new CustomerResponse(
            customer.getId(),
            customer.getName(),
            customer.getSegment(),
            customer.getSegment().discount()
        ))
        .toList();
  }

  private ProductResponse toProduct(Product product, Customer listCustomer) {
    ListPrice listPrice = context.requireListPrice(product.getSku());
    PriceRule rule = context.requireRule(product.getCategory());
    Quote quote = context.quote(product, listCustomer, listPrice, rule);
    var competitor = context.latestCompetitor(product.getSku());
    String status = recommendations
        .findTopBySkuAndCustomerIdOrderByCreatedAtDesc(product.getSku(), listCustomer.getId())
        .map(PriceRecommendation::getStatus)
        .map(Enum::name)
        .orElse(null);
    return new ProductResponse(
        product.getSku(),
        product.getName(),
        product.getCategory(),
        product.getUnit(),
        product.getCost(),
        listPrice.getAmount(),
        competitor.map(c -> c.getAmount()).orElse(null),
        competitor.map(c -> c.getCompetitor()).orElse(null),
        quote.recommended(),
        status
    );
  }
}
