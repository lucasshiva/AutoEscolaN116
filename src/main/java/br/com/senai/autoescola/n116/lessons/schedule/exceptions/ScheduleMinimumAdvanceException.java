package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleMinimumAdvanceException extends ApiException {
	public static final String code = "SCHEDULE_MIN_ADVANCE";

	public ScheduleMinimumAdvanceException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, "Lessons can only be scheduled at least 30 minutes in advance");
	}
}
