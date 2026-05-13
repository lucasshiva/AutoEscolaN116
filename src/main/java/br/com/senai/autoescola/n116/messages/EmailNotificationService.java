package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.lessons.DrivingLesson;
import br.com.senai.autoescola.n116.messages.events.EmailEvent;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailNotificationService {
	private static final String SCHOOL_EMAIL = "driving.school@gmail.com";

	private final EmailEventPublisher emailEventPublisher;
	private final TemplateEngine templateEngine;

	public EmailNotificationService(
			EmailEventPublisher emailEventPublisher,
			TemplateEngine templateEngine
	) {
		this.emailEventPublisher = emailEventPublisher;
		this.templateEngine = templateEngine;
	}

	public void sendNotificationForLesson(DrivingLesson lesson, String action) {
		List<String> emails = List.of(SCHOOL_EMAIL, lesson.getStudent().getEmail(), lesson.getInstructor().getEmail());
		String assunto = "Driving lesson " + action;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
		LocalDateTime data = LocalDateTime.of(lesson.getSchedule().getDate(), lesson.getSchedule().getStartHour());
		String formattedDate = data.format(formatter);
		
		// Thymeleaf template
		Context context = new Context();
		context.setVariable("action", action);
		context.setVariable("id", lesson.getId().toString());
		context.setVariable("student", lesson.getStudent().getNome());
		context.setVariable("instructor", lesson.getInstructor().getNome());
		context.setVariable("date", formattedDate);

		// Same name as html file
		String conteudo = templateEngine.process("schedule_lesson", context);

		EmailEvent event = new EmailEvent(lesson.getId(), emails, assunto, conteudo);
		emailEventPublisher.publish(event);
	}
}
