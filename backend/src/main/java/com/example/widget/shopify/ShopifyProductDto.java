package com.example.widget.shopify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Minimal representations of the subset of Shopify's /products.json schema we consume.
 * Shopify returns many fields we don't use; ignoring unknowns at the type level keeps the
 * mapping focused.
 */
public class ShopifyProductDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Variant {
        public String price;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Product {
        public Long id;
        public String title;
        public String body_html;
        public String product_type;
        public List<Variant> variants;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        public List<Product> products;
    }
}
