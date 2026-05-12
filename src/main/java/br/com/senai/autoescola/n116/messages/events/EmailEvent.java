package br.com.senai.autoescola.n116.messages.events;

import java.util.List;

public record EmailEvent(Long ocorrencia, List<String> emails, String assunto, String conteudo) {
}
