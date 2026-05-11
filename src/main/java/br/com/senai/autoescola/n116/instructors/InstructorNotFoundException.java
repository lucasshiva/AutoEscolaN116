package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class InstructorNotFoundException extends ApiException {
	public static final String code = "INSTRUCTOR_NOT_FOUND";

	public InstructorNotFoundException(Long id) {
		super(HttpStatus.NOT_FOUND, code, "Instructor with given ID not found");
	}
}
