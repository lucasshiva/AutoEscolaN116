package br.com.senai.autoescola.n116.instructors.getById;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.common.models.Especialidade;

import java.time.Instant;

public record GetInstructorByIdResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String cnh,
        Especialidade especialidade,
        Address endereco,
        Instant createdAt,
        Instant updatedAt
) {
}
