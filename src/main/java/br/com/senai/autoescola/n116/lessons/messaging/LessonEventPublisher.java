package br.com.senai.autoescola.n116.lessons.messaging;

import br.com.senai.autoescola.n116.lessons.events.LessonScheduledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonEventPublisher {
	private final RabbitTemplate rabbitTemplate;
	private final LessonRabbitProperties props;

	public void publishSchedule(LessonScheduledEvent event) {
		rabbitTemplate.convertAndSend(
				props.exchange(),
				props.routingKey(),
				event
		);
	}
}
