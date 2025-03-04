package com.nw.intern.bu3internecommerce.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseData<T> {
    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

}
