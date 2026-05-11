package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleOutsideOfWorkingHoursException extends ApiException {
	public static final String code = "SCHEDULE_INVALID_HOUR";
	private static final String message = "Cannot schedule lessons outside of working hours";

	public ScheduleOutsideOfWorkingHoursException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, message);
	}
}
