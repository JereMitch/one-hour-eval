package com.example.widget.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopifyId;

    private String title;

    @Column(length = 4000)
    private String description;

    private BigDecimal price;

    private String category;

    @Enumerated(EnumType.STRING)
    private Level level;

    public Product() {}

    public Product(Long shopifyId, String title, String description, BigDecimal price, String category, Level level) {
        this.shopifyId = shopifyId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.level = level;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getShopifyId() { return shopifyId; }
    public void setShopifyId(Long shopifyId) { this.shopifyId = shopifyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }
}
