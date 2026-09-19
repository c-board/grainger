package com.grainger.pricing.api.web;

import com.grainger.pricing.api.dto.PriceBreakdownResponse;
import com.grainger.pricing.api.service.PriceQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PriceController {

  private final PriceQueryService prices;

  public PriceController(PriceQueryService prices) {
    this.prices = prices;
  }

  @GetMapping("/prices")
  public PriceBreakdownResponse prices(@RequestParam String sku, @RequestParam String customerId) {
    return prices.breakdown(sku, customerId);
  }
}
