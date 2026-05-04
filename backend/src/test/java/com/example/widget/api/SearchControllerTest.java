package com.example.widget.api;

import com.example.widget.product.Level;
import com.example.widget.product.Product;
import com.example.widget.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ProductRepository repository;

    @Test
    void queryReturnsMatches() throws Exception {
        Product p = new Product(1L, "Running Shoes", "Lightweight",
                new BigDecimal("89.99"), "Footwear", Level.BEGINNER);
        given(repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(any(), any()))
                .willReturn(List.of(p));

        mvc.perform(get("/api/products/search").param("q", "running"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Running Shoes"));
    }

    @Test
    void emptyQueryReturnsAll() throws Exception {
        Product p1 = new Product(1L, "A", "x", BigDecimal.ONE, "c", Level.BEGINNER);
        Product p2 = new Product(2L, "B", "y", BigDecimal.ONE, "c", Level.EXPERT);
        given(repository.findAll()).willReturn(List.of(p1, p2));

        mvc.perform(get("/api/products/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
