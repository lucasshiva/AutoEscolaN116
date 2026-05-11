package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends ApiException {
	public static final String code = "STUDENT_NOT_FOUND";

	public StudentNotFoundException(Long id) {
		super(HttpStatus.NOT_FOUND, code, "Student with given ID was not found");
	}
}
