package br.com.senai.autoescola.n116.common.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        var errors = ex
                .getFieldErrors()
                .stream()
                .map(e -> new ValidationError(e.getField(), e.getDefaultMessage())).toList();

        return ResponseEntity.badRequest().body(new ValidationErrorResponse(errors));
    }

    public record ValidationErrorResponse(List<ValidationError> errors) {
    }

    public record ValidationError(String field, String message) {
    }
}