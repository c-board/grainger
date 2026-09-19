package com.grainger.pricing.engine.service;

import com.grainger.pricing.core.PricingCalculator;
import com.grainger.pricing.core.Quote;
import com.grainger.pricing.core.QuoteRequest;
import com.grainger.pricing.domain.AppliedPrice;
import com.grainger.pricing.domain.AppliedPriceId;
import com.grainger.pricing.domain.CompetitorPrice;
import com.grainger.pricing.domain.Customer;
import com.grainger.pricing.domain.ListPrice;
import com.grainger.pricing.domain.PriceFact;
import com.grainger.pricing.domain.PriceRecommendation;
import com.grainger.pricing.domain.PriceRule;
import com.grainger.pricing.domain.Product;
import com.grainger.pricing.domain.RecommendationStatus;
import com.grainger.pricing.domain.repo.AppliedPriceRepository;
import com.grainger.pricing.domain.repo.CompetitorPriceRepository;
import com.grainger.pricing.domain.repo.CustomerRepository;
import com.grainger.pricing.domain.repo.ListPriceRepository;
import com.grainger.pricing.domain.repo.PriceFactRepository;
import com.grainger.pricing.domain.repo.PriceRecommendationRepository;
import com.grainger.pricing.domain.repo.PriceRuleRepository;
import com.grainger.pricing.domain.repo.ProductRepository;
import com.grainger.pricing.engine.kafka.RecommendationPublisher;
import com.grainger.pricing.events.CompetitorPriceObserved;
import com.grainger.pricing.events.PriceRecommendationCreated;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceRecalculationService {

  private static final Logger log = LoggerFactory.getLogger(PriceRecalculationService.class);

  private final ProductRepository products;
  private final CustomerRepository customers;
  private final ListPriceRepository listPrices;
  private final PriceRuleRepository priceRules;
  private final CompetitorPriceRepository competitorPrices;
  private final AppliedPriceRepository appliedPrices;
  private final PriceRecommendationRepository recommendations;
  private final PriceFactRepository priceFacts;
  private final RecommendationPublisher publisher;

  public PriceRecalculationService(
      ProductRepository products,
      CustomerRepository customers,
      ListPriceRepository listPrices,
      PriceRuleRepository priceRules,
      CompetitorPriceRepository competitorPrices,
      AppliedPriceRepository appliedPrices,
      PriceRecommendationRepository recommendations,
      PriceFactRepository priceFacts,
      RecommendationPublisher publisher
  ) {
    this.products = products;
    this.customers = customers;
    this.listPrices = listPrices;
    this.priceRules = priceRules;
    this.competitorPrices = competitorPrices;
    this.appliedPrices = appliedPrices;
    this.recommendations = recommendations;
    this.priceFacts = priceFacts;
    this.publisher = publisher;
  }

  @Transactional
  public void handle(CompetitorPriceObserved event) {
    Product product = products.findById(event.sku()).orElse(null);
    if (product == null) {
      log.warn("Ignoring competitor tick for unknown SKU {}", event.sku());
      return;
    }

    Instant observedAt = event.observedAt() == null ? Instant.now() : event.observedAt();
    competitorPrices.save(new CompetitorPrice(event.sku(), event.competitor(), event.amount(), observedAt));

    ListPrice listPrice = listPrices.findById(product.getSku()).orElse(null);
    PriceRule rule = priceRules.findByCategory(product.getCategory()).orElse(null);
    if (listPrice == null || rule == null) {
      log.warn("Missing list price or rule for {}", product.getSku());
      return;
    }

    for (Customer customer : customers.findAll()) {
      recalculate(product, customer, listPrice, rule, event);
    }
  }

  private void recalculate(
      Product product,
      Customer customer,
      ListPrice listPrice,
      PriceRule rule,
      CompetitorPriceObserved event
  ) {
    BigDecimal current = appliedPrices.findById(new AppliedPriceId(product.getSku(), customer.getId()))
        .map(AppliedPrice::getAmount)
        .orElseGet(() -> PricingCalculator.money(
            listPrice.getAmount().multiply(BigDecimal.ONE.subtract(customer.getSegment().discount()))
        ));

    Quote quote = PricingCalculator.quote(new QuoteRequest(
        product.getCost(),
        rule.getMinMargin(),
        listPrice.getAmount(),
        customer.getSegment().discount(),
        event.amount(),
        rule.getCompetitorUndercutThreshold(),
        current
    ));

    if (quote.recommended().compareTo(current) == 0) {
      return;
    }

    Instant now = Instant.now();
    recommendations.findBySkuAndCustomerIdAndStatus(
            product.getSku(),
            customer.getId(),
            RecommendationStatus.PENDING_APPROVAL
        )
        .forEach(pending -> pending.supersede(now));

    RecommendationStatus status = quote.autoApply()
        ? RecommendationStatus.AUTO_APPLIED
        : RecommendationStatus.PENDING_APPROVAL;

    PriceRecommendation recommendation = new PriceRecommendation(
        UUID.randomUUID(),
        product.getSku(),
        customer.getId(),
        current,
        quote.recommended(),
        quote.floor(),
        event.amount(),
        quote.deltaPct(),
        status,
        quote.rationale(),
        now
    );
    recommendations.save(recommendation);

    if (status == RecommendationStatus.AUTO_APPLIED) {
      AppliedPriceId key = new AppliedPriceId(product.getSku(), customer.getId());
      AppliedPrice applied = appliedPrices.findById(key)
          .orElseGet(() -> new AppliedPrice(key, quote.recommended(), "AUTO", now));
      applied.apply(quote.recommended(), "AUTO", now);
      appliedPrices.save(applied);
    }

    priceFacts.save(new PriceFact(
        product.getSku(),
        customer.getSegment().name(),
        listPrice.getAmount(),
        quote.recommended(),
        event.amount(),
        quote.floor(),
        status.name(),
        now
    ));

    publisher.publish(new PriceRecommendationCreated(
        recommendation.getId(),
        recommendation.getSku(),
        recommendation.getCustomerId(),
        recommendation.getCurrentPrice(),
        recommendation.getRecommendedPrice(),
        recommendation.getFloorPrice(),
        recommendation.getCompetitorPrice(),
        recommendation.getDeltaPct(),
        recommendation.getStatus().name(),
        recommendation.getRationale(),
        recommendation.getCreatedAt()
    ));
  }
}
