package com.example.widget.recommend;

import com.example.widget.product.Level;
import com.example.widget.product.Product;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "app.llm.provider", havingValue = "mock", matchIfMissing = true)
public class MockRecommendationService implements RecommendationService {

    @Override
    public String recommend(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "";
        }
        Product beginner = firstWithLevel(products, Level.BEGINNER);
        Product expert = firstWithLevel(products, Level.EXPERT);

        if (beginner != null && expert != null) {
            return String.format(
                    "Based on your search, the %s is perfect for beginners due to its ease of use, " +
                    "while the %s offers the advanced features an expert needs.",
                    beginner.getTitle(), expert.getTitle());
        }
        if (products.size() >= 2) {
            return String.format(
                    "Comparing these picks: the %s and the %s each suit different needs — " +
                    "consider your experience level when choosing.",
                    products.get(0).getTitle(), products.get(1).getTitle());
        }
        return String.format("The %s is a solid choice based on your search.", products.get(0).getTitle());
    }

    private static Product firstWithLevel(List<Product> products, Level level) {
        for (Product p : products) {
            if (p.getLevel() == level) return p;
        }
        return null;
    }
}
