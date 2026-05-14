package br.com.senai.autoescola.n116.lessons.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "app.rabbit.lesson")
@ConfigurationPropertiesScan
public record LessonRabbitProperties(String exchange, String queue, String routingKey) {
}