package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleTooSoonException extends RuntimeException {
    public ScheduleTooSoonException(long minutes) {
        super("You can only schedule lessons with a minimum of " + minutes + " minutes in advance");
    }
}
