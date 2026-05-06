package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleInvalidDayException extends RuntimeException {
    public static String message = "You cannot schedule a class on Sundays";

    public ScheduleInvalidDayException() {
        super(message);
    }
}
