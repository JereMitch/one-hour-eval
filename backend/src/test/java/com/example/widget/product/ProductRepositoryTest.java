package com.example.widget.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void persistsAndFindsBySubstring() {
        repository.save(new Product(1L, "Running Shoes", "Lightweight trainers for the road",
                new BigDecimal("89.99"), "Footwear", Level.BEGINNER));
        repository.save(new Product(2L, "Hiking Boots", "Ankle support for rough terrain",
                new BigDecimal("149.00"), "Footwear", Level.EXPERT));

        List<Product> hits = repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "running", "running");

        assertThat(hits).hasSize(1);
        assertThat(hits.get(0).getTitle()).isEqualTo("Running Shoes");
    }

    @Test
    void findAllReturnsEverything() {
        repository.save(new Product(3L, "Yoga Mat", "Non-slip surface",
                new BigDecimal("29.99"), "Fitness", Level.BEGINNER));
        repository.save(new Product(4L, "Climbing Harness", "Certified for advanced routes",
                new BigDecimal("79.00"), "Climbing", Level.EXPERT));

        assertThat(repository.findAll()).hasSize(2);
    }
}
