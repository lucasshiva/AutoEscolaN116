package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.messages.events.EmailEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublishEmailEvent implements EmailEventPublisher {
	private final RabbitTemplate rabbitTemplate;

	public RabbitMQPublishEmailEvent(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void publish(EmailEvent dto) {
		rabbitTemplate.convertAndSend("email", dto);
	}
}
