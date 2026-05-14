package br.com.senai.autoescola.n116.lessons.actions.schedule.exceptions;

import br.com.senai.autoescola.n116.common.errors.ApiException;
import org.springframework.http.HttpStatus;

public class ScheduleTooManyLessonsOnTheSameDayException extends ApiException {
	public static final String code = "SCHEDULE_TOO_MANY_LESSONS_ON_THE_SAME_DAY";

	public ScheduleTooManyLessonsOnTheSameDayException() {
		super(HttpStatus.UNPROCESSABLE_CONTENT, code, "Students can only have one lesson per day");
	}
}
