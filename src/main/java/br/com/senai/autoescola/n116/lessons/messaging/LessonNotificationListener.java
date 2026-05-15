package br.com.senai.autoescola.n116.lessons.messaging;

import br.com.senai.autoescola.n116.common.mail.SmtpEmailSender;
import br.com.senai.autoescola.n116.lessons.events.LessonScheduledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class LessonNotificationListener {
	private final SmtpEmailSender smtpEmailSender;

	@RabbitListener(queues = "${app.rabbit.lesson.queue}")
	public void onScheduled(LessonScheduledEvent event) {
		IO.println("Lesson " + event.lessonId() + " scheduled at " + event.date());
		List<String> emails = List.of(event.instructorEmail(), event.studentEmail());
		for (String email : emails) {
			String body = "A lesson has been scheduled on " + event.date().getDayOfWeek() + "." +
					"\nDate: " + event.date() +
					"\nStudent: " + event.studentName() +
					"\nInstructor: " + event.instructorName();
			smtpEmailSender.send(email, "Lesson scheduled", body);
		}
	}
}
