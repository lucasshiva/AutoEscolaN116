package br.com.senai.autoescola.n116.students.create;

import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateStudentRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email(message = "the provided email address is invalid")
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{11}$", message = "must be exactly 11 digits")
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{11}$", message = "must be exactly 11 digits")
        String cpf,

        @NotNull
        Address endereco
) {
}