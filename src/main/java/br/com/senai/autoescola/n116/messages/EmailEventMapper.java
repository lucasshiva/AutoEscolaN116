package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.messages.dto.EmailEventDto;
import br.com.senai.autoescola.n116.messages.events.EmailEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailEventMapper {
	public EmailEvent toEvent(EmailEventDto msg) {
		return new EmailEvent(
				msg.ocorrencia(),
				msg.emails(),
				msg.assunto(),
				msg.conteudo()
		);
	}
}
