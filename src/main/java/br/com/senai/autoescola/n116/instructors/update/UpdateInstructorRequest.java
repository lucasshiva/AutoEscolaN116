package br.com.senai.autoescola.n116.instructors.update;

import br.com.senai.autoescola.n116.common.annotations.Telefone;
import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateInstructorRequest(
        @NotBlank
        String nome,

        @Telefone
        @NotBlank
        String telefone,

        @NotNull
        Address endereco
) {
}
