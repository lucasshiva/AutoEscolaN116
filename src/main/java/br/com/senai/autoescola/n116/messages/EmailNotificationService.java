package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.lessons.DrivingLesson;
import br.com.senai.autoescola.n116.messages.events.EmailEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailNotificationService {
	private static final String SCHOOL_EMAIL = "driving.school@gmail.com";

	private final EmailEventPublisher emailEventPublisher;

	public EmailNotificationService(EmailEventPublisher emailEventPublisher) {
		this.emailEventPublisher = emailEventPublisher;
	}

	public void sendNotificationForLesson(DrivingLesson lesson, String action) {
		List<String> emails = List.of(SCHOOL_EMAIL, lesson.getStudent().getEmail(), lesson.getInstructor().getEmail());
		String assunto = "Driving lesson " + action;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
		LocalDateTime data = LocalDateTime.of(lesson.getSchedule().getDate(), lesson.getSchedule().getStartHour());
		String formattedDate = data.format(formatter);


		String conteudo = "\nA new driving lesson has been " + action + "!" +
				"\nID: " + lesson.getId() +
				"\nStudent: " + lesson.getStudent().getNome() +
				"\nInstructor: " + lesson.getInstructor().getNome() +
				"\nDate: " + formattedDate;

		EmailEvent event = new EmailEvent(lesson.getId(), emails, assunto, conteudo);
		emailEventPublisher.publish(event);
	}
}
