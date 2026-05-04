package br.com.senai.autoescola.n116.auth.login;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank
        String login,

        @NotBlank
        String senha
) {
}
