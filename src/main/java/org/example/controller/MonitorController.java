package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.repository.ProductRepository;
import org.example.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MonitorController {
    private final ProductRepository productRepository;
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/monitor")
    public String monitor(Model model) {
        model.addAttribute("changes", productRepository.findRecentPriceChanges());
        return "monitor";
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("analytics", analyticsService.getAnalytics());
        return "analytics";
    }

    @GetMapping("/alerts")
    public String alertsPage() {
        return "alerts";
    }
}
