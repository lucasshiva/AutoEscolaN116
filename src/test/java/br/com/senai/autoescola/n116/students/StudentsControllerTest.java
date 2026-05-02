package br.com.senai.autoescola.n116.students;


import br.com.senai.autoescola.n116.common.errors.GlobalExceptionHandler;
import br.com.senai.autoescola.n116.students.builders.CreateStudentCommandBuilder;
import br.com.senai.autoescola.n116.students.create.CreateStudentCommand;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class StudentsControllerTest {
    @Autowired
    private RestTestClient testClient;
    @Autowired
    private StudentsRepository studentsRepository;

    @BeforeEach
    void setUp() {
        studentsRepository.deleteAll();
    }

    @Test
    void shouldCreateUserWithValidDTO() {
        var command = new CreateStudentCommandBuilder().build();

        var result = testClient.post().uri("/students").body(command).exchange();

        result.expectStatus().isCreated();
        result
                .expectBody(CreateStudentResponse.class)
                .value(body -> {
                    assertThat(body).isNotNull();
                    assertThat(body.createdAt())
                            .isInstanceOf(Instant.class)
                            .isBefore(Instant.now())
                            .isAfter(Instant.now().minusSeconds(5));
                    assertThat(body.id()).isNotNull();
                    assertThat(studentsRepository.findById(body.id())).isPresent();
                });

    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "123456789012345"})
    void shouldRejectInvalidCPFLength(String cpf) {
        assertSingleValidationError(
                new CreateStudentCommandBuilder().withCpf(cpf).build(),
                "cpf", "must be exactly 11 digits"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12345",
            "123456789012345"
    })
    void shouldRejectInvalidPhoneLength(String phone) {
        assertSingleValidationError(
                new CreateStudentCommandBuilder().withPhone(phone).build(),
                "telefone", "must be exactly 11 digits"
        );
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertSingleValidationError(
                new CreateStudentCommandBuilder().withEmail("wrongEmail").build(),
                "email", "the provided email address is invalid"
        );
    }

    @Test
    void shouldRejectWithMissingAddress() {
        assertSingleValidationError(
                new CreateStudentCommandBuilder().withAddress(null).build(),
                "endereco", "must not be null"
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectEmptyOrBlankName(String name) {
        assertSingleValidationError(
                new CreateStudentCommandBuilder().withName(name).build(),
                "nome", "must not be blank"
        );
    }

    private void assertSingleValidationError(
            CreateStudentCommand command, String expectedField, String expectedMessage) {
        var response = testClient.post().uri("/students").body(command).exchange();
        response.expectStatus().isBadRequest();
        response.expectBody(GlobalExceptionHandler.ValidationErrorResponse.class).value(body -> {
            assertThat(body).isNotNull();
            assertThat(body.errors()).hasSize(1);
            var error = body.errors().getFirst();
            assertThat(error.field()).isEqualTo(expectedField);
            assertThat(error.message()).isEqualTo(expectedMessage);
        });
    }
}