package br.com.senai.autoescola.n116.students.update;

import br.com.senai.autoescola.n116.common.models.Address;

import java.time.Instant;

public record UpdateStudentResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Address endereco,
        Instant createdAt,
        Instant updatedAt
) {
}
