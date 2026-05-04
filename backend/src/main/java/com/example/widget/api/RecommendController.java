package com.example.widget.api;

import com.example.widget.product.Product;
import com.example.widget.product.ProductRepository;
import com.example.widget.product.ProductSelector;
import com.example.widget.recommend.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecommendController {

    private final ProductRepository repository;
    private final ProductSelector selector;
    private final RecommendationService recommendationService;

    public RecommendController(ProductRepository repository,
                               ProductSelector selector,
                               RecommendationService recommendationService) {
        this.repository = repository;
        this.selector = selector;
        this.recommendationService = recommendationService;
    }

    public record RecommendResponse(List<Product> products, String recommendation) {}

    @GetMapping("/recommend")
    public RecommendResponse recommend(@RequestParam(name = "q", required = false, defaultValue = "") String q) {
        List<Product> matches = (q == null || q.isBlank())
                ? repository.findAll()
                : repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
        List<Product> chosen = selector.selectThree(matches);
        String recommendation = chosen.isEmpty() ? "" : recommendationService.recommend(chosen);
        return new RecommendResponse(chosen, recommendation);
    }
}
