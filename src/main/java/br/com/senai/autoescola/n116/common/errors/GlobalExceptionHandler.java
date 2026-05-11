package br.com.senai.autoescola.n116.common.errors;

import br.com.senai.autoescola.n116.auth.UserAlreadyExistsException;
import br.com.senai.autoescola.n116.instructors.DuplicateCnhException;
import br.com.senai.autoescola.n116.students.DuplicateCpfException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
			@NonNull MethodArgumentNotValidException ex,
			@NonNull HttpHeaders headers,
			@NonNull HttpStatusCode status,
			@NonNull WebRequest request
	) {
		ProblemDetail pd = ex.getBody();

		pd.setTitle("Validation failed");
		pd.setDetail("One or more fields are invalid");

		var errors = ex
				.getFieldErrors()
				.stream()
				.map(error -> Map.of(
						"field", error.getField(),
						"message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "Unknown error"
				))
				.toList();

		pd.setProperty("errors", errors);
		return handleExceptionInternal(
				ex,
				pd,
				headers,
				status,
				request
		);

	}

	@ExceptionHandler({DuplicateCnhException.class, DuplicateCpfException.class})
	public ProblemDetail handleDuplicateCnh(RuntimeException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(ApiException.class)
	public ProblemDetail handleApiException(ApiException ex) {
		var pd = ProblemDetail.forStatusAndDetail(ex.status, ex.getMessage());
		pd.setProperty("code", ex.code);
		return pd;
	}
}