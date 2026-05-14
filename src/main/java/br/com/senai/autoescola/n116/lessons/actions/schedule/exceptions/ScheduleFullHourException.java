package br.com.senai.autoescola.n116.lessons.actions.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleFullHourException extends ApiException {
	public final static String code = "SCHEDULE_FULL_HOUR";
	private final static String message = "Lessons can only be scheduled in full hours (e.g 8AM, 9AM, etc.)";

	public ScheduleFullHourException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, message);
	}
}
