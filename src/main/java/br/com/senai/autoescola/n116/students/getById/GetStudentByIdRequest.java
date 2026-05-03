package br.com.senai.autoescola.n116.students.getById;

import jakarta.validation.constraints.NotNull;

public record GetStudentByIdRequest(
        @NotNull
        Long id
) {
}
