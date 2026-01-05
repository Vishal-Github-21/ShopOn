package com.example.shopon.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class UpdateProductRequest {

    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Boolean active;

    private Set<Long> categoryIds;
}
