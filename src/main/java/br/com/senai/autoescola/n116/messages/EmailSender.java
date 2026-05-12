package br.com.senai.autoescola.n116.messages;

public interface EmailSender {
	void send(String subject, String message, String to);
}
