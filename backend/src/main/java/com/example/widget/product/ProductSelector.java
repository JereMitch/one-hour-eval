package com.example.widget.product;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ProductSelector {

    public List<Product> selectThree(List<Product> searchResults) {
        if (searchResults == null || searchResults.isEmpty()) {
            return List.of();
        }

        List<Product> beginners = filterByLevel(searchResults, Level.BEGINNER);
        List<Product> experts = filterByLevel(searchResults, Level.EXPERT);

        List<Product> chosen = new ArrayList<>(3);
        Product beginner = pickRandom(beginners);
        Product expert = pickRandom(experts);

        if (beginner != null) chosen.add(beginner);
        if (expert != null) chosen.add(expert);

        for (Product p : searchResults) {
            if (chosen.size() == 3) break;
            if (!chosen.contains(p)) {
                chosen.add(p);
            }
        }
        return chosen;
    }

    private static List<Product> filterByLevel(List<Product> products, Level level) {
        List<Product> out = new ArrayList<>();
        for (Product p : products) {
            if (p.getLevel() == level) out.add(p);
        }
        return out;
    }

    private static Product pickRandom(List<Product> products) {
        if (products.isEmpty()) return null;
        return products.get(ThreadLocalRandom.current().nextInt(products.size()));
    }
}
