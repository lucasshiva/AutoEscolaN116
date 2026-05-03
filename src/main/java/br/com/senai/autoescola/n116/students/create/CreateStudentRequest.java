package br.com.senai.autoescola.n116.students.create;

import br.com.senai.autoescola.n116.common.annotations.CPF;
import br.com.senai.autoescola.n116.common.annotations.Telefone;
import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateStudentRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email(message = "the provided email address is invalid")
        String email,

        @Telefone
        String telefone,

        @CPF
        String cpf,

        @NotNull
        Address endereco
) {
}