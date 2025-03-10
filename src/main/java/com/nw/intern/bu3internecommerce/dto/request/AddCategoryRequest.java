package com.nw.intern.bu3internecommerce.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddCategoryRequest {
    private String name;
}
