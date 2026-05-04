package com.example.widget.api;

import com.example.widget.product.Product;
import com.example.widget.product.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class SearchController {

    private final ProductRepository repository;

    public SearchController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam(name = "q", required = false, defaultValue = "") String q) {
        if (q == null || q.isBlank()) {
            return repository.findAll();
        }
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
    }
}
