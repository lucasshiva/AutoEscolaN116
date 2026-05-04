package br.com.senai.autoescola.n116.instructors.getById;

import jakarta.validation.constraints.NotNull;

public record GetInstructorByIdRequest(
        @NotNull
        Long id
) {
}
