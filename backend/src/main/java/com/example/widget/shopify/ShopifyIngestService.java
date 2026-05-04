package com.example.widget.shopify;

import com.example.widget.product.Level;
import com.example.widget.product.Product;
import com.example.widget.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ShopifyIngestService implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ShopifyIngestService.class);

    private final RestTemplate restTemplate;
    private final ProductRepository repository;
    private final String storeUrl;

    public ShopifyIngestService(RestTemplate restTemplate,
                                ProductRepository repository,
                                @Value("${shopify.store-url}") String storeUrl) {
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.storeUrl = storeUrl;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (storeUrl == null || storeUrl.isBlank() || storeUrl.contains("REPLACE_ME")) {
            log.warn("shopify.store-url is not configured (current value: {}). Skipping ingest. " +
                    "Set it in application.properties to seed products.", storeUrl);
            return;
        }
        try {
            String url = storeUrl.replaceAll("/$", "") + "/products.json?limit=250";
            log.info("Ingesting products from {}", url);
            ShopifyProductDto.Response resp = restTemplate.getForObject(url, ShopifyProductDto.Response.class);
            if (resp == null || resp.products == null || resp.products.isEmpty()) {
                log.warn("No products returned from {}", url);
                return;
            }
            List<Product> mapped = new ArrayList<>(resp.products.size());
            for (ShopifyProductDto.Product p : resp.products) {
                mapped.add(toProduct(p));
            }
            repository.saveAll(mapped);
            log.info("Ingested {} products into H2", mapped.size());
        } catch (Exception ex) {
            log.error("Failed to ingest products from {}: {}. Continuing with empty DB.", storeUrl, ex.getMessage());
        }
    }

    private static Product toProduct(ShopifyProductDto.Product p) {
        BigDecimal price = BigDecimal.ZERO;
        if (p.variants != null && !p.variants.isEmpty() && p.variants.get(0).price != null) {
            try {
                price = new BigDecimal(p.variants.get(0).price);
            } catch (NumberFormatException ignored) {
                // leave as zero
            }
        }
        Level level = ThreadLocalRandom.current().nextBoolean() ? Level.BEGINNER : Level.EXPERT;
        return new Product(
                p.id,
                p.title,
                stripHtml(p.body_html),
                price,
                p.product_type == null ? "" : p.product_type,
                level
        );
    }

    private static String stripHtml(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
    }
}
