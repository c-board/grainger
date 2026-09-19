package com.grainger.pricing.api.web;

import com.grainger.pricing.api.dto.RecommendationResponse;
import com.grainger.pricing.api.service.RecommendationService;
import com.grainger.pricing.domain.RecommendationStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

  private final RecommendationService recommendations;

  public RecommendationController(RecommendationService recommendations) {
    this.recommendations = recommendations;
  }

  @GetMapping
  public List<RecommendationResponse> list(
      @RequestParam(defaultValue = "PENDING_APPROVAL") RecommendationStatus status
  ) {
    return recommendations.list(status);
  }

  @PostMapping("/{id}/approve")
  public RecommendationResponse approve(@PathVariable UUID id) {
    return recommendations.approve(id);
  }

  @PostMapping("/{id}/reject")
  public RecommendationResponse reject(@PathVariable UUID id) {
    return recommendations.reject(id);
  }
}
