package br.com.senai.autoescola.n116.instructors.list;

import br.com.senai.autoescola.n116.common.models.Especialidade;

public record ListInstructorDTO(
        String nome,
        String email,
        String cnh,
        Especialidade especialidade
) {
}
