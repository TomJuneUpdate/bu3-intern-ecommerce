package com.nw.intern.bu3internecommerce.exception;

import com.nw.intern.bu3internecommerce.config.locale.Translator;
import com.nw.intern.bu3internecommerce.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNotFoundException(ResourceNotFoundException e) {
        return ApiResponse.fail(e.getMessage());
    }

    // Xử lý MethodArgumentNotValidException (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiResponse.fail("Validation Failed", errors);
    }
    // Xử lý tất cả các exception khác
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGlobalException(Exception ex, WebRequest request) {
        // Log lỗi ra và ẩn đi message thực sự
        ErrorDetails errorDetails = new ErrorDetails();
        return ApiResponse.fail(Translator.toLocale("error.server"));
    }
}
