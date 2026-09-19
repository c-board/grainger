package com.grainger.pricing.api.web;

import com.grainger.pricing.api.dto.CustomerResponse;
import com.grainger.pricing.api.dto.ProductResponse;
import com.grainger.pricing.api.service.CatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CatalogController {

  private final CatalogService catalog;

  public CatalogController(CatalogService catalog) {
    this.catalog = catalog;
  }

  @GetMapping("/products")
  public List<ProductResponse> products() {
    return catalog.listProducts();
  }

  @GetMapping("/products/{sku}")
  public ProductResponse product(@PathVariable String sku) {
    return catalog.getProduct(sku);
  }

  @GetMapping("/customers")
  public List<CustomerResponse> customers() {
    return catalog.listCustomers();
  }
}
