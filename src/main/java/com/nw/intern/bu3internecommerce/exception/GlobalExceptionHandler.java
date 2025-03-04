package com.nw.intern.bu3internecommerce.exception;

import com.nw.intern.bu3internecommerce.config.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(new Date());
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setPath(request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    // Xử lý MethodArgumentNotValidException (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
    // Xử lý tất cả các exception khác
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        // Log lỗi ra và ẩn đi message thực sự
        ErrorDetails errorDetails = new ErrorDetails();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Translator.toLocale("error.server"));
    }
}
