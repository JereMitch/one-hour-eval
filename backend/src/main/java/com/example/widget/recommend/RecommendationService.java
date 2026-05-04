package com.example.widget.recommend;

import com.example.widget.product.Product;

import java.util.List;

public interface RecommendationService {
    String recommend(List<Product> products);
}
