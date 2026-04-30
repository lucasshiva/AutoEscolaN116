package br.com.senai.autoescola.n116.students.create;

import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateStudentCommand(
        @NotBlank
        String nome,

        @NotBlank
        String email,

        @NotBlank
        @Length(max = 11)
        String telefone,

        @NotBlank
        @Length(max = 11)
        String cpf,

        @NotNull
        Address endereco
) {
}