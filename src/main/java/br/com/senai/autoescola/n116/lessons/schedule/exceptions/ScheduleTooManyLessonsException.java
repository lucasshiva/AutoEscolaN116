package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleTooManyLessonsException extends RuntimeException {
    public ScheduleTooManyLessonsException() {
        super("Students cannot have more than one lesson per day");
    }
}
