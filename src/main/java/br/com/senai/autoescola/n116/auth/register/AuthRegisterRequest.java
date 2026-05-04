package br.com.senai.autoescola.n116.auth.register;

import jakarta.validation.constraints.NotBlank;

public record AuthRegisterRequest(
        @NotBlank
        String login,

        @NotBlank
        String senha
) {
}
