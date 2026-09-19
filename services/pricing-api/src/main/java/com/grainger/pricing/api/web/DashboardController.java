package com.grainger.pricing.api.web;

import com.grainger.pricing.api.dto.DashboardResponse;
import com.grainger.pricing.api.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

  private final DashboardService dashboard;

  public DashboardController(DashboardService dashboard) {
    this.dashboard = dashboard;
  }

  @GetMapping("/dashboard")
  public DashboardResponse dashboard() {
    return dashboard.dashboard();
  }
}
