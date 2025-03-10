package com.nw.intern.bu3internecommerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryDto {
    private String code;
    private String name;
}
