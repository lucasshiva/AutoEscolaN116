package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleFullHourException extends RuntimeException {
    public ScheduleFullHourException() {
        super("Lessons can only be scheduled in full hours (e.g 8AM, 9AM, etc.)");
    }
}
