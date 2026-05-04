package com.example.widget.recommend;

import com.example.widget.product.Level;
import com.example.widget.product.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MockRecommendationServiceTest {

    private final MockRecommendationService service = new MockRecommendationService();

    @Test
    void identifiesBeginnerAndExpertByLevel() {
        Product beginner = new Product(1L, "EZ Trainer", "easy to use",
                new BigDecimal("49"), "Footwear", Level.BEGINNER);
        Product expert = new Product(2L, "Pro Racer", "advanced",
                new BigDecimal("199"), "Footwear", Level.EXPERT);
        Product extra = new Product(3L, "Mid Shoe", "neutral",
                new BigDecimal("99"), "Footwear", Level.EXPERT);

        String text = service.recommend(List.of(beginner, expert, extra));

        assertThat(text)
                .contains("EZ Trainer")
                .contains("Pro Racer")
                .contains("beginners")
                .contains("expert");
    }

    @Test
    void emptyInputReturnsEmptyString() {
        assertThat(service.recommend(List.of())).isEmpty();
    }

    @Test
    void fallsBackWhenNoBeginnerOrExpertCombo() {
        Product onlyExpert1 = new Product(1L, "Pro A", "x", BigDecimal.TEN, "c", Level.EXPERT);
        Product onlyExpert2 = new Product(2L, "Pro B", "y", BigDecimal.TEN, "c", Level.EXPERT);
        String text = service.recommend(List.of(onlyExpert1, onlyExpert2));
        assertThat(text).contains("Pro A").contains("Pro B");
    }
}
