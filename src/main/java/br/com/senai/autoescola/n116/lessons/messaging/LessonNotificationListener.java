package br.com.senai.autoescola.n116.lessons.messaging;

import br.com.senai.autoescola.n116.lessons.events.LessonScheduledEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class LessonNotificationListener {

	@RabbitListener(queues = "${app.rabbit.lesson.queue}")
	public void onScheduled(LessonScheduledEvent event) {
		IO.println("Lesson " + event.lessonId() + " scheduled at " + event.date());
	}
}
