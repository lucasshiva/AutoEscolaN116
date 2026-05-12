package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.messages.dto.EmailEventDto;
import br.com.senai.autoescola.n116.messages.events.EmailEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailEventConsumer {
	private final EmailEventMapper emailEventMapper;
	private final EmailSender emailSender;

	public EmailEventConsumer(
			EmailEventMapper emailEventMapper,
			EmailSender emailSender
	) {
		this.emailEventMapper = emailEventMapper;
		this.emailSender = emailSender;
	}

	@RabbitListener(queues = "email")
	public void receiveMessage(EmailEventDto dto) {
		EmailEvent event = emailEventMapper.toEvent(dto);
		for (String to : event.emails()) {
			emailSender.send(event.assunto(), event.conteudo(), to);
		}
	}
}
