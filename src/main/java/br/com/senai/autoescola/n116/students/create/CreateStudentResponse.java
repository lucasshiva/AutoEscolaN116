package br.com.senai.autoescola.n116.students.create;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateStudentResponse(
        Long id,
        @NotNull
        Instant createdAt
) {
}