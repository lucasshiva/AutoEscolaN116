package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleNoInstructorsAvailableException extends ApiException {
	public static final String code = "SCHEDULE_NO_INSTRUCTORS_AVAILABLE";
	private static final String msg = "No instructors available for a driving lesson in the specified date and time";

	public ScheduleNoInstructorsAvailableException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, msg);
	}
}
