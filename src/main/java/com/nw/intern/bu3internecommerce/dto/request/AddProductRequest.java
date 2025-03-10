package com.nw.intern.bu3internecommerce.dto.request;

import com.nw.intern.bu3internecommerce.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Getter
@Setter
public class AddProductRequest {
    private String name;
    @Min(0)
    private Double mrpPrice;
    @Min(0)
    private Double sellingPrice;
    @Min(1)
    private Integer quantity;
    @Min(0)
    private int discountPercentage;
    private String description;
    @NotBlank
    private String category;
    private Set<String> color;
    private List<String> imageUrls = new ArrayList<String>();
    private Set<String> sizes;
}
