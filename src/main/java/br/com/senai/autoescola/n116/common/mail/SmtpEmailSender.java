package br.com.senai.autoescola.n116.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SmtpEmailSender {
	private final JavaMailSender sender;

	public void send(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		try {
			sender.send(message);
		} catch (MailException e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}
}
