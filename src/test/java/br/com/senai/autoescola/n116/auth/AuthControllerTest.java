package br.com.senai.autoescola.n116.auth;

import br.com.senai.autoescola.n116.auth.login.AuthLoginRequest;
import br.com.senai.autoescola.n116.auth.login.AuthLoginResponse;
import br.com.senai.autoescola.n116.auth.register.AuthRegisterRequest;
import br.com.senai.autoescola.n116.auth.register.AuthRegisterResponse;
import br.com.senai.autoescola.n116.security.JwtTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private JwtTokenService tokenService;

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

    @Nested
    public class Register {
        @Test
        public void shouldRegister() {
            var request = new AuthRegisterRequest(normalUserLogin, normalUserPassword);
            testClient.post().uri("/auth/register")
                    .body(request)
                    .exchangeSuccessfully()
                    .expectStatus().isOk()
                    .expectBody(AuthRegisterResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.token()).isNotBlank();
                        assertThat(tokenService.isTokenValidForLogin(body.token(), request.login()));
                    });
        }

        @Test
        @DisplayName("Throw an error when trying to register with a login that already exists")
        public void sameLogin() {
            jdbcTemplate.update(
                    "INSERT INTO users (login, senha) VALUES (?, ?)",
                    normalUserLogin, passwordEncoder.encode(normalUserPassword)
            );
            var request = new AuthRegisterRequest(normalUserLogin, "anotherpassword");
            testClient.post().uri("/auth/register")
                    .body(request)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT);
        }
    }
}