package br.com.senai.autoescola.n116.instructors.update;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.common.models.Especialidade;

import java.time.Instant;

public record UpdateInstructorResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Address endereco,
        Especialidade especialidade,
        Instant createdAt,
        Instant updatedAt
) {
}