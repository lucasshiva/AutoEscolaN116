package br.com.senai.autoescola.n116.common.errors;

import br.com.senai.autoescola.n116.instructors.DuplicateCnhException;
import br.com.senai.autoescola.n116.students.DuplicateCpfException;
import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Void> handleStudentNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({DuplicateCnhException.class, DuplicateCpfException.class})
    public ResponseEntity<Map<String, String>> handleDuplicateCnh(RuntimeException ex) {
        return ResponseEntity.unprocessableContent().body(Map.of("message", ex.getMessage()));
    }

    public record ValidationErrorResponse(List<ValidationError> errors) {
    }

    public record ValidationError(String field, String message) {
    }
}