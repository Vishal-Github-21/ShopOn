package com.example.shopon.dto.request;

import lombok.Data;


import java.math.BigDecimal;
import java.util.Set;

@Data
public class CreateProductRequest {

    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;

    // Selected category IDs
    private Set<Long> categoryIds;
}
