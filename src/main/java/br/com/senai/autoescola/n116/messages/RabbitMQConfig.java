package br.com.senai.autoescola.n116.messages;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	@Bean
	public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
		return new JacksonJsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		var rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jacksonJsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Queue emailEventQueue() {
		return new Queue("email", true);
	}

	@Bean
	public EmailNotificationService emailNotificationService(EmailEventPublisher eventPublisher) {
		// Call Timeleaf later
		return new EmailNotificationService(eventPublisher);
	}
}
