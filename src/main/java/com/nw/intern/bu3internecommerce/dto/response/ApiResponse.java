package com.nw.intern.bu3internecommerce.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    public static final String SUCCESS = "000000";
    public static final String FAIL = "999999";

    private String code;
    private String msg;
    @JsonIgnore
    private T data;

    public static <T> ApiResponse<T> ok() {
        return restResult(null, SUCCESS, "SUCCESS");
    }

    public static <T> ApiResponse<T> ok(T data) {
        return restResult(data, SUCCESS, "SUCCESS");
    }

    public static <T> ApiResponse<T> fail() {
        return restResult(null, FAIL, "FAIL");
    }

    public static <T> ApiResponse<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> ApiResponse<T> fail(String msg, T data) {
        return restResult(data, FAIL, msg);
    }

    private static <T> ApiResponse<T> restResult(T data, String code, String msg) {
        ApiResponse<T> r = new ApiResponse<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    public static <T> Boolean isError(ApiResponse<T> ret) {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(ApiResponse<T> ret) {
        return ApiResponse.SUCCESS.equals(ret.getCode());
    }
}


