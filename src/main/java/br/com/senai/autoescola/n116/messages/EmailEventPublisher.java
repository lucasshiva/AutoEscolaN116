package br.com.senai.autoescola.n116.messages;

import br.com.senai.autoescola.n116.messages.events.EmailEvent;

public interface EmailEventPublisher {
	void publish(EmailEvent dto);
}
