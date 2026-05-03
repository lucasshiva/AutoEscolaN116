package br.com.senai.autoescola.n116.students.getById;

import br.com.senai.autoescola.n116.common.models.Address;

import java.time.Instant;

public record GetStudentByIdResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        Address endereco,
        String cpf,
        Instant createdAt,
        Instant updatedAt
) {
}
