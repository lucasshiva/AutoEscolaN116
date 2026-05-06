package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleInstructorNotAvailableException extends RuntimeException {
    public ScheduleInstructorNotAvailableException(Long id) {
        super("Instructor with id " + id + " is not available");
    }
}
