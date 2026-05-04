package com.example.widget.recommend;

import com.example.widget.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.llm.provider", havingValue = "claude")
public class ClaudeRecommendationService implements RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(ClaudeRecommendationService.class);
    private static final String ENDPOINT = "https://api.anthropic.com/v1/messages";
    private static final String SYSTEM_PROMPT =
            "You compare ecommerce products in two short sentences for a shopper. " +
            "Focus on which is better for a beginner versus an expert. No fluff, no lists.";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String model;

    public ClaudeRecommendationService(RestTemplate restTemplate,
                                       @Value("${anthropic.api-key}") String apiKey,
                                       @Value("${anthropic.model}") String model) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public String recommend(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return "";
        }
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("ANTHROPIC_API_KEY not set; returning empty recommendation.");
            return "";
        }

        StringBuilder userMsg = new StringBuilder("Compare these products:\n");
        for (Product p : products) {
            userMsg.append("- ").append(p.getTitle())
                   .append(" [").append(p.getLevel()).append("]: ")
                   .append(truncate(p.getDescription(), 240))
                   .append('\n');
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "max_tokens", 256,
                "system", SYSTEM_PROMPT,
                "messages", List.of(Map.of("role", "user", "content", userMsg.toString()))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> resp = restTemplate.postForObject(ENDPOINT, new HttpEntity<>(body, headers), Map.class);
            if (resp == null) return "";
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> content = (List<Map<String, Object>>) resp.get("content");
            if (content == null || content.isEmpty()) return "";
            Object text = content.get(0).get("text");
            return text == null ? "" : text.toString();
        } catch (Exception ex) {
            log.error("Claude call failed: {}", ex.getMessage());
            return "";
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
