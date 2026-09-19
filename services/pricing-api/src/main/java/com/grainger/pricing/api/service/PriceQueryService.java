package com.grainger.pricing.api.service;

import com.grainger.pricing.api.dto.DerivationStep;
import com.grainger.pricing.api.dto.PriceBreakdownResponse;
import com.grainger.pricing.core.Quote;
import com.grainger.pricing.domain.Customer;
import com.grainger.pricing.domain.ListPrice;
import com.grainger.pricing.domain.PriceRule;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.repo.CustomerRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PriceQueryService {

  private final CustomerRepository customers;
  private final PricingContextService context;

  public PriceQueryService(CustomerRepository customers, PricingContextService context) {
    this.customers = customers;
    this.context = context;
  }

  @Transactional(readOnly = true)
  public PriceBreakdownResponse breakdown(String sku, String customerId) {
    Product product = context.requireProduct(sku);
    Customer customer = customers.findById(customerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown customer " + customerId));
    ListPrice listPrice = context.requireListPrice(sku);
    PriceRule rule = context.requireRule(product.getCategory());
    Quote quote = context.quote(product, customer, listPrice, rule);
    var competitor = context.latestCompetitor(sku);

    List<DerivationStep> steps = List.of(
        new DerivationStep("Cost", product.getCost(), "Unit cost from the product master."),
        new DerivationStep("Floor", quote.floor(), "Cost × (1 + " + rule.getMinMargin() + " min margin)."),
        new DerivationStep("List", listPrice.getAmount(), "Published list price."),
        new DerivationStep(
            "Segment",
            quote.segmentPrice(),
            customer.getSegment() + " discount of " + customer.getSegment().discount() + " off list."
        ),
        new DerivationStep(
            "Competitor",
            competitor.map(c -> c.getAmount()).orElse(null),
            competitor.map(c -> c.getCompetitor() + " latest observed price.").orElse("No competitor tick yet.")
        ),
        new DerivationStep("Recommended", quote.recommended(), quote.rationale())
    );

    return new PriceBreakdownResponse(
        product.getSku(),
        product.getName(),
        customer.getId(),
        customer.getName(),
        customer.getSegment().name(),
        product.getCost(),
        rule.getMinMargin(),
        quote.floor(),
        listPrice.getAmount(),
        customer.getSegment().discount(),
        quote.segmentPrice(),
        competitor.map(c -> c.getCompetitor()).orElse(null),
        competitor.map(c -> c.getAmount()).orElse(null),
        rule.getCompetitorUndercutThreshold(),
        context.currentPrice(product, customer, listPrice),
        context.currentSource(product, customer),
        quote.recommended(),
        quote.matchedCompetitor(),
        quote.rationale(),
        steps
    );
  }
}
