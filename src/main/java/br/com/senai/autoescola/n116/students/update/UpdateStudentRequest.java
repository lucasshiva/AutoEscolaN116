package br.com.senai.autoescola.n116.students.update;

import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateStudentRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "must be exactly 11 digits")
        String telefone,

        @NotNull
        Address endereco
) {
}
