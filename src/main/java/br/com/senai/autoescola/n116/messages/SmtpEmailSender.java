package br.com.senai.autoescola.n116.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmtpEmailSender implements EmailSender {
	private final JavaMailSender mailSender;

	public SmtpEmailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void send(String subject, String conteudo, String to) {
		try {
			var message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(conteudo);
			mailSender.send(message);
			IO.println("Email enviado com sucesso para " + to);
			log.info("Email enviado com sucesso!");

		} catch (Exception e) {
			System.err.println("Erro ao enviar o email para " + to);
			e.printStackTrace();
		}
	}
}
