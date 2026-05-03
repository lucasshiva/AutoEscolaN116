package br.com.senai.autoescola.n116.students.update;

import br.com.senai.autoescola.n116.common.annotations.Telefone;
import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateStudentRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Telefone
        String telefone,

        @NotNull
        Address endereco
) {
}
