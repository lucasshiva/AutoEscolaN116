package br.com.senai.autoescola.n116.lessons.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LessonsRabbitConfig {
	private final LessonRabbitProperties props;

	@Bean
	TopicExchange lessonExchange() {
		return new TopicExchange(props.exchange());
	}

	@Bean
	Queue lessonScheduledQueue() {
		return new Queue(props.queue(), true);
	}

	@Bean
	Binding lessonBinding() {
		return BindingBuilder
				.bind(lessonScheduledQueue())
				.to(lessonExchange())
				.with(props.routingKey());
	}
}
