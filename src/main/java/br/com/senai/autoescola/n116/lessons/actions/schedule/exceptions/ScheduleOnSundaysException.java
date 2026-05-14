package br.com.senai.autoescola.n116.lessons.actions.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleOnSundaysException extends ApiException {
	public static final String code = "SCHEDULE_ON_SUNDAYS";
	private static final String message = "Classes can only be scheduled from monday to saturday";

	public ScheduleOnSundaysException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, message);
	}
}
