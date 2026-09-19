package com.grainger.pricing.api.service;

import com.grainger.pricing.core.PricingCalculator;
import com.grainger.pricing.core.Quote;
import com.grainger.pricing.core.QuoteRequest;
import com.grainger.pricing.domain.AppliedPrice;
import com.grainger.pricing.domain.AppliedPriceId;
import com.grainger.pricing.domain.CompetitorPrice;
import com.grainger.pricing.domain.Customer;
import com.grainger.pricing.domain.ListPrice;
import com.grainger.pricing.domain.PriceRule;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.repo.AppliedPriceRepository;
import com.grainger.pricing.domain.repo.CompetitorPriceRepository;
import com.grainger.pricing.domain.repo.ListPriceRepository;
import com.grainger.pricing.domain.repo.PriceRuleRepository;
import com.grainger.pricing.domain.repo.ProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PricingContextService {

  private final ProductRepository products;
  private final ListPriceRepository listPrices;
  private final PriceRuleRepository priceRules;
  private final CompetitorPriceRepository competitorPrices;
  private final AppliedPriceRepository appliedPrices;

  public PricingContextService(
      ProductRepository products,
      ListPriceRepository listPrices,
      PriceRuleRepository priceRules,
      CompetitorPriceRepository competitorPrices,
      AppliedPriceRepository appliedPrices
  ) {
    this.products = products;
    this.listPrices = listPrices;
    this.priceRules = priceRules;
    this.competitorPrices = competitorPrices;
    this.appliedPrices = appliedPrices;
  }

  public Product requireProduct(String sku) {
    return products.findById(sku)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown SKU " + sku));
  }

  public ListPrice requireListPrice(String sku) {
    return listPrices.findById(sku)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No list price for " + sku));
  }

  public PriceRule requireRule(String category) {
    return priceRules.findByCategory(category)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No rule for " + category));
  }

  public Optional<CompetitorPrice> latestCompetitor(String sku) {
    return competitorPrices.findFirstBySkuOrderByObservedAtDesc(sku);
  }

  public BigDecimal currentPrice(Product product, Customer customer, ListPrice listPrice) {
    return appliedPrices.findById(new AppliedPriceId(product.getSku(), customer.getId()))
        .map(AppliedPrice::getAmount)
        .orElseGet(() -> PricingCalculator.money(
            listPrice.getAmount().multiply(BigDecimal.ONE.subtract(customer.getSegment().discount()))
        ));
  }

  public String currentSource(Product product, Customer customer) {
    return appliedPrices.findById(new AppliedPriceId(product.getSku(), customer.getId()))
        .map(AppliedPrice::getSource)
        .orElse("LIST");
  }

  public Quote quote(Product product, Customer customer, ListPrice listPrice, PriceRule rule) {
    Optional<CompetitorPrice> competitor = latestCompetitor(product.getSku());
    return PricingCalculator.quote(new QuoteRequest(
        product.getCost(),
        rule.getMinMargin(),
        listPrice.getAmount(),
        customer.getSegment().discount(),
        competitor.map(CompetitorPrice::getAmount).orElse(null),
        rule.getCompetitorUndercutThreshold(),
        currentPrice(product, customer, listPrice)
    ));
  }
}
