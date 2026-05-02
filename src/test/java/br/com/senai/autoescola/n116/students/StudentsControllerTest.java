package br.com.senai.autoescola.n116.students;


import br.com.senai.autoescola.n116.common.errors.GlobalExceptionHandler;
import br.com.senai.autoescola.n116.students.builders.CreateStudentRequestBuilder;
import br.com.senai.autoescola.n116.students.builders.StudentBuilder;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Ensure we're not ignoring soft-deleted records.
        jdbcTemplate.execute("TRUNCATE TABLE students");
    }

    private void assertSingleValidationError(
            CreateStudentRequest command, String expectedField, String expectedMessage) {
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

    @Nested
    class CreateStudent {
        @Test
        void shouldCreateUserWithValidDTO() {
            var command = new CreateStudentRequestBuilder().build();

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
                    new CreateStudentRequestBuilder().withCpf(cpf).build(),
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
                    new CreateStudentRequestBuilder().withPhone(phone).build(),
                    "telefone", "must be exactly 11 digits"
            );
        }

        @Test
        void shouldRejectInvalidEmail() {
            assertSingleValidationError(
                    new CreateStudentRequestBuilder().withEmail("wrongEmail").build(),
                    "email", "the provided email address is invalid"
            );
        }

        @Test
        void shouldRejectWithMissingAddress() {
            assertSingleValidationError(
                    new CreateStudentRequestBuilder().withAddress(null).build(),
                    "endereco", "must not be null"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldRejectEmptyOrBlankName(String name) {
            assertSingleValidationError(
                    new CreateStudentRequestBuilder().withName(name).build(),
                    "nome", "must not be blank"
            );
        }
    }

    @Nested
    class DeleteStudent {
        @Test
        @DisplayName("SoftDelete works automatically via Hibernate.")
        public void shouldSoftDelete() {
            var student = new StudentBuilder().build();

            var saved = studentsRepository.save(student);

            var response = testClient.delete().uri("/students/{id}", saved.getId()).exchange();
            response.expectStatus().isNoContent();

            // Hibernate filters won't apply here - raw SQL sees everything
            var deletedAt = jdbcTemplate.queryForObject(
                    "SELECT deleted_at FROM students WHERE id = ?",
                    Instant.class,
                    saved.getId()
            );

            // Hibernate sees nothing - it manages soft deletion automatically.
            assertThat(studentsRepository.count()).isZero();

            // But the row is still there.
            assertThat(deletedAt).isNotNull();
        }

        @Test
        @DisplayName("Returns a 404 Not Found when the ID doesn't exist.")
        public void missingId() {
            var response = testClient.delete().uri("/students/{id}", 1).exchange();
            response.expectStatus().isNotFound();
        }
    }
}