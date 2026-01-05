package com.example.shopon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public static interface NameStep {
        DescriptionStep withName(String name);
    }

    public static interface DescriptionStep {
        PriceStep withDescription(String description);
    }

    public static interface PriceStep {
        ImageUrlStep withPrice(BigDecimal price);
    }

    public static interface ImageUrlStep {
        ActiveStep withImageUrl(String imageUrl);
    }

    public static interface ActiveStep {
        CategoriesStep withActive(boolean active);
    }

    public static interface CategoriesStep {
        BuildStep withCategories(Set<Category> categories);
    }

    public static interface BuildStep {
        Product build();
    }


    public static class Builder implements NameStep, DescriptionStep, PriceStep, ImageUrlStep, ActiveStep, CategoriesStep, BuildStep {
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private boolean active;
        private Set<Category> categories;

        private Builder() {
        }

        public static NameStep product() {
            return new Builder();
        }

        @Override
        public DescriptionStep withName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public PriceStep withDescription(String description) {
            this.description = description;
            return this;
        }

        @Override
        public ImageUrlStep withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        @Override
        public ActiveStep withImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        @Override
        public CategoriesStep withActive(boolean active) {
            this.active = active;
            return this;
        }

        @Override
        public BuildStep withCategories(Set<Category> categories) {
            this.categories = categories;
            return this;
        }

        @Override
        public Product build() {
            Product product = new Product();
            product.setName(this.name);
            product.setDescription(this.description);
            product.setPrice(this.price);
            product.setImageUrl(this.imageUrl);
            product.setActive(this.active);
            product.setCategories(this.categories);
            return product;
        }
    }
}

