package br.com.senai.autoescola.n116.messages.dto;

import java.util.List;

public record EmailEventDto(
		Long ocorrencia,
		List<String> emails,
		String assunto,
		String conteudo
) {
}
