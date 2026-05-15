package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.common.mail.SmtpEmailSender;
import br.com.senai.autoescola.n116.lessons.events.LessonScheduledEvent;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestConfiguration
public class LessonTestRabbitConfig {
	@Bean
	public TestConsumer testConsumer() {
		return new TestConsumer();
	}

	@Getter
	public static class TestConsumer {
		@Autowired
		private SmtpEmailSender smtpEmailSender;

		private final List<LessonScheduledEvent> events = Collections.synchronizedList(new ArrayList<>());

		@RabbitListener(queues = "${app.rabbit.lesson.queue}")
		public void listen(LessonScheduledEvent event) {
			IO.println("Adding event to events list for testing");
			smtpEmailSender.send(event.instructorEmail(), "New lesson", "A new lesson has been scheduled");
			smtpEmailSender.send(event.studentEmail(), "Lesson scheduled", "Lesson scheduled successfully");
			events.add(event);
		}
	}
}