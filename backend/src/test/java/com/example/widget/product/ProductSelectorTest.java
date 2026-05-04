package com.example.widget.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductSelectorTest {

    private final ProductSelector selector = new ProductSelector();

    @Test
    void picksOneBeginnerOneExpertOneExtra() {
        List<Product> input = buildMixed();
        List<Product> chosen = selector.selectThree(input);

        assertThat(chosen).hasSize(3);
        assertThat(chosen).filteredOn(p -> p.getLevel() == Level.BEGINNER).hasSizeGreaterThanOrEqualTo(1);
        assertThat(chosen).filteredOn(p -> p.getLevel() == Level.EXPERT).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void returnsEmptyOnEmptyInput() {
        assertThat(selector.selectThree(List.of())).isEmpty();
    }

    @Test
    void returnsAllAvailableWhenLessThanThree() {
        List<Product> input = List.of(
                product(1L, "A", Level.BEGINNER),
                product(2L, "B", Level.EXPERT)
        );
        assertThat(selector.selectThree(input)).hasSize(2);
    }

    @Test
    void handlesNoBeginnersGracefully() {
        List<Product> onlyExperts = List.of(
                product(1L, "X", Level.EXPERT),
                product(2L, "Y", Level.EXPERT),
                product(3L, "Z", Level.EXPERT)
        );
        List<Product> chosen = selector.selectThree(onlyExperts);
        assertThat(chosen).hasSize(3);
        assertThat(chosen).allMatch(p -> p.getLevel() == Level.EXPERT);
    }

    private static List<Product> buildMixed() {
        List<Product> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add(product((long) i, "Beginner-" + i, Level.BEGINNER));
        }
        for (int i = 6; i <= 10; i++) {
            list.add(product((long) i, "Expert-" + i, Level.EXPERT));
        }
        return list;
    }

    private static Product product(Long id, String title, Level level) {
        Product p = new Product(id, title, "desc", new BigDecimal("10.00"), "cat", level);
        p.setId(id);
        return p;
    }
}
