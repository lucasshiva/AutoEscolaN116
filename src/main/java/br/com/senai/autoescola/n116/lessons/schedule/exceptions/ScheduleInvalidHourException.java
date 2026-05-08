package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

import java.time.LocalTime;

public class ScheduleInvalidHourException extends RuntimeException {
	public ScheduleInvalidHourException(LocalTime requestedHour, LocalTime minHour, LocalTime maxHour) {
		super("Invalid hour '" + requestedHour + "' for schedule time " + minHour + " ~ " + maxHour);
	}
}
