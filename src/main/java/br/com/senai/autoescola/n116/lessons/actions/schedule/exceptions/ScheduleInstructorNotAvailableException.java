package br.com.senai.autoescola.n116.lessons.actions.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleInstructorNotAvailableException extends ApiException {
	public static final String code = "SCHEDULE_SPECIFIED_INSTRUCTOR_NOT_AVAILABLE";

	public ScheduleInstructorNotAvailableException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, "The instructor is not available");
	}
}
