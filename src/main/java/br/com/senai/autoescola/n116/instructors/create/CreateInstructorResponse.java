package br.com.senai.autoescola.n116.instructors.create;

import java.time.Instant;

public record CreateInstructorResponse(
        Long id,
        String nome,
        Instant createdAt
) {
}
