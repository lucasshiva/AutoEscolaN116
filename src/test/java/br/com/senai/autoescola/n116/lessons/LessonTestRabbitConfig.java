package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.lessons.events.LessonScheduledEvent;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestConfiguration
@Primary
public class LessonTestRabbitConfig {
	@Bean
	public TestConsumer testConsumer() {
		return new TestConsumer();
	}

	@Getter
	public static class TestConsumer {
		private final List<LessonScheduledEvent> events = Collections.synchronizedList(new ArrayList<>());

		@RabbitListener(queues = "${app.rabbit.lesson.queue}")
		public void listen(LessonScheduledEvent event) {
			IO.println("Adding event to events list for testing");
			events.add(event);
		}
	}
}