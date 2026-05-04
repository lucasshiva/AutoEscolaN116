package br.com.senai.autoescola.n116.instructors.create;

import br.com.senai.autoescola.n116.common.annotations.CNH;
import br.com.senai.autoescola.n116.common.annotations.Telefone;
import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.common.models.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInstructorRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email(message = "the provided email address is invalid")
        String email,

        @Telefone
        String telefone,

        @CNH
        String cnh,

        @NotNull
        Especialidade especialidade,

        @NotNull
        Address endereco
) {
}
