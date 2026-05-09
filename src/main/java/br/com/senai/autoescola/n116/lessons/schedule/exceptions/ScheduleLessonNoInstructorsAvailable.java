package br.com.senai.autoescola.n116.lessons.schedule.exceptions;

public class ScheduleLessonNoInstructorsAvailable extends RuntimeException {
	public ScheduleLessonNoInstructorsAvailable() {
		super("There are no instructors available for this driving lesson");
	}
}
