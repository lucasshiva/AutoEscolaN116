package br.com.senai.autoescola.n116.auth;

import br.com.senai.autoescola.n116.auth.login.AuthLoginRequest;
import br.com.senai.autoescola.n116.auth.login.AuthLoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class AuthControllerTest {
    private final String normalUserLogin = "user";
    private final String normalUserPassword = "12345";

    @Autowired
    private RestTestClient testClient;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }


    @Nested
    public class Login {
        @Test
        public void shouldLoginWithValidCredentials() {
            jdbcTemplate.update(
                    "INSERT INTO users (login, senha) VALUES (?, ?)",
                    normalUserLogin, passwordEncoder.encode(normalUserPassword)
            );

            testClient.post().uri("/auth/login")
                    .body(new AuthLoginRequest(normalUserLogin, normalUserPassword))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(AuthLoginResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.token()).isNotBlank();
                    });
        }

        @Test
        public void shouldFailWithInvalidUsername() {
            jdbcTemplate.update(
                    "INSERT INTO users (login, senha) VALUES (?, ?)",
                    "invalid", passwordEncoder.encode(normalUserPassword)
            );

            testClient.post().uri("/auth/login")
                    .body(new AuthLoginRequest(normalUserLogin, normalUserPassword))
                    .exchange()
                    .expectStatus().isUnauthorized();
        }

        @Test
        public void shouldFailWithInvalidPassword() {
            jdbcTemplate.update(
                    "INSERT INTO users (login, senha) VALUES (?, ?)",
                    normalUserLogin, passwordEncoder.encode("wrongPassword")
            );

            testClient.post().uri("/auth/login")
                    .body(new AuthLoginRequest(normalUserLogin, normalUserPassword))
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }
}