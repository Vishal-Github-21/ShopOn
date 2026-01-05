package com.example.shopon.service;

import com.example.shopon.dto.request.CreateProductRequest;
import com.example.shopon.dto.request.UpdateProductRequest;
import com.example.shopon.dto.response.CategoryResponse;
import com.example.shopon.dto.response.ProductResponse;
import com.example.shopon.entity.Category;
import com.example.shopon.entity.Product;
import com.example.shopon.exception.ProductNotFoundException;
import com.example.shopon.repository.CategoryRepository;
import com.example.shopon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        public java.util.List<ProductResponse> getAllProducts() {
                return productRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        public ProductResponse getProductById(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductNotFoundException(id));
                return mapToResponse(product);
        }

        public ProductResponse createProduct(CreateProductRequest request) {

                Set<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());

                Product product = Product.Builder.product()
                                .withName(request.getName())
                                .withDescription(request.getDescription())
                                .withPrice(request.getPrice())
                                .withImageUrl(request.getImageUrl())
                                .withActive(true)
                                .withCategories(categories)
                                .build();

                Product savedProduct = productRepository.save(product);

                return mapToResponse(savedProduct);
        }

        public ProductResponse updateProduct(Long id, UpdateProductRequest request) {

                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductNotFoundException(id));

                if (request.getName() != null) {
                        product.setName(request.getName());
                }

                if (request.getDescription() != null) {
                        product.setDescription(request.getDescription());
                }

                if (request.getPrice() != null) {
                        product.setPrice(request.getPrice());
                }

                if (request.getImageUrl() != null) {
                        product.setImageUrl(request.getImageUrl());
                }

                if (request.getActive() != null) {
                        product.setActive(request.getActive());
                }

                if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
                        Set<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());
                        product.setCategories(categories);
                }

                Product updatedProduct = productRepository.save(product);

                return mapToResponse(updatedProduct);
        }

        public void deleteProduct(Long id) {
                Product product = productRepository.findById(id)
                                .orElseThrow(() -> new ProductNotFoundException(id));

                // Soft delete
                product.setActive(false);
                productRepository.save(product);
        }

        private ProductResponse mapToResponse(Product product) {

                return ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .imageUrl(product.getImageUrl())
                                .active(product.isActive())
                                .categories(product.getCategories().stream().map(
                                                            category -> CategoryResponse.builder()
                                                                    .id(category.getId())
                                                                    .name(category.getName())
                                                                    .build())
                                                                    .collect(Collectors.toSet())
                                                    )
                                .build();
        }
}