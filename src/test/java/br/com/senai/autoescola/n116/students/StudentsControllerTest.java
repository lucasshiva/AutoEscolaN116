package br.com.senai.autoescola.n116.students;


import br.com.senai.autoescola.n116.IntegrationTestBase;
import br.com.senai.autoescola.n116.students.builders.CreateStudentRequestBuilder;
import br.com.senai.autoescola.n116.students.builders.StudentBuilder;
import br.com.senai.autoescola.n116.students.builders.UpdateStudentRequestBuilder;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import br.com.senai.autoescola.n116.students.getById.GetStudentByIdResponse;
import br.com.senai.autoescola.n116.students.list.ListStudentsResponse;
import br.com.senai.autoescola.n116.students.update.UpdateStudentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static br.com.senai.autoescola.n116.utils.ControllerTestUtils.assertCreatedWithTimestamp;
import static br.com.senai.autoescola.n116.utils.ControllerTestUtils.assertValidationError;
import static org.assertj.core.api.Assertions.assertThat;

class StudentsControllerTest extends IntegrationTestBase {
    @Autowired
    private StudentsRepository studentsRepository;

    @Nested
    class CreateStudent {
        private final CreateStudentRequestBuilder requestBuilder =
                new CreateStudentRequestBuilder();

        private final RestTestClient.RequestBodySpec spec = testClient.post().uri("/students");

        @Test
        void shouldCreateStudentWithValidPayload() {
            var request = requestBuilder.build();
            assertCreatedWithTimestamp(
                    spec.body(request),
                    CreateStudentResponse.class,
                    CreateStudentResponse::createdAt,
                    body -> assertThat(studentsRepository.findById(body.id())).isPresent()
            );
        }

        @Test
        void shouldRejectDuplicateCPF() {
            var first = requestBuilder.build();
            var second = requestBuilder.withCpf(first.cpf()).build();

            spec.body(first).exchange().expectStatus().isCreated();
            spec.body(second).exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "123456789012345"})
        void shouldRejectInvalidCPFLength(String cpf) {
            assertValidationError("cpf", spec.body(requestBuilder.withCpf(cpf).build()));
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "12345",
                "123456789012345"
        })
        void shouldRejectInvalidTelefoneLength(String telefone) {
            assertValidationError("telefone", spec.body(requestBuilder.withTelefone(telefone).build()));
        }

        @Test
        void shouldRejectInvalidEmail() {
            assertValidationError("email", spec.body(requestBuilder.withEmail("invalidEmail").build()));
        }

        @Test
        void shouldRejectWithMissingAddress() {
            assertValidationError("endereco", spec.body(requestBuilder.withAddress(null).build()));
        }

        @ParameterizedTest
        @NullAndEmptySource
        void shouldRejectEmptyOrBlankName(String nome) {
            assertValidationError("nome", spec.body(requestBuilder.withName(nome).build()));
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

    @Nested
    class ListStudents {
        @Test
        public void emptyList() {
            testClient.get().uri("/students").exchange()
                    .expectStatus().isOk()
                    .expectBody(ListStudentsResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.data()).isEmpty();
                        assertThat(body.totalPages()).isZero();
                        assertThat(body.totalElements()).isZero();
                    });
        }

        @Test
        public void shouldReturnAllStudents() {
            var builder = new StudentBuilder();
            studentsRepository.saveAll(List.of(
                    builder.build(),
                    builder.build(),
                    builder.build()
            ));

            testClient.get().uri("/students").exchange()
                    .expectStatus().isOk()
                    .expectBody(ListStudentsResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.data()).hasSize(3);
                        assertThat(body.totalPages()).isOne();
                        assertThat(body.totalElements()).isEqualTo(3);
                    });
        }

        @Test
        void shouldNotIncludeDeletedStudents() {
            var builder = new StudentBuilder();
            Student first = builder.build();
            Student second = builder.build();

            studentsRepository.saveAll(List.of(
                    first,
                    second
            ));

            // Delete first student
            testClient.delete().uri("/students/{id}", first.getId()).exchange();

            // Ensure we only have one student in the response
            testClient.get().uri("/students").exchange()
                    .expectStatus().isOk()
                    .expectBody(ListStudentsResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.data()).hasSize(1);
                        assertThat(body.totalElements()).isEqualTo(1);
                    });
        }

        @Test
        void shouldReturnCorrectPaginationMetadata() {
            var builder = new StudentBuilder();
            studentsRepository.saveAll(
                    Stream.generate(builder::build)
                            .limit(15)
                            .toList()
            );

            testClient.get().uri("/students?page=1&size=10").exchange()
                    .expectStatus().isOk()
                    .expectBody(ListStudentsResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.data()).hasSize(10);
                        assertThat(body.totalElements()).isEqualTo(15);
                        assertThat(body.totalPages()).isEqualTo(2);
                        assertThat(body.page()).isOne();
                        assertThat(body.size()).isEqualTo(10);
                    });
        }
    }

    @Nested
    class GetStudent {
        @Test
        public void shouldReturnCorrectStudent() {
            var builder = new StudentBuilder();
            Student student = builder.build();
            studentsRepository.save(student);

            testClient.get().uri("/students/{id}", student.getId()).exchange()
                    .expectStatus().isOk()
                    .expectBody(GetStudentByIdResponse.class)
                    .value(body -> {
                        assertThat(body).isNotNull();
                        assertThat(body.id()).isEqualTo(student.getId());
                    });
        }

        @Test
        public void shouldNotFindStudent() {
            testClient.get().uri("/students/{id}", 1).exchange().expectStatus().isNotFound();
        }
    }

    @Nested
    class EditStudent {
        private RestTestClient.RequestBodySpec specWithFixedId() {
            return testClient.put().uri("/students/{id}", 1);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        public void shouldEditStudent() {
            var student = new StudentBuilder().build();
            var request = new UpdateStudentRequestBuilder().build();

            studentsRepository.save(student);
            var response = testClient.put().uri("/students/{id}", student.getId()).body(request).exchange();
            response.expectStatus().isOk();

            response.expectBody(UpdateStudentResponse.class).value(body -> {
                assertThat(body).isNotNull();
                // Check if we're returning the body correctly.
                assertThat(request)
                        .usingRecursiveComparison()
                        .comparingOnlyFields("nome", "telefone", "endereco")
                        .isEqualTo(body);

                // Check if we're persisting changes correctly.
                var opt = studentsRepository.findById(student.getId());
                if (opt.isEmpty())
                    throw new RuntimeException("Could not find student in repository");

                var persisted = opt.get();
                assertThat(request)
                        .usingRecursiveComparison()
                        .comparingOnlyFields("nome", "telefone", "endereco")
                        .isEqualTo(persisted);
            });
        }


        @ParameterizedTest
        @ValueSource(strings = {"123", "1232132144242"})
        public void shouldRejectTelefoneWithWrongLength(String telefone) {
            var request = new UpdateStudentRequestBuilder().withTelefone(telefone).build();
            assertValidationError("telefone", specWithFixedId().body(request));
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void shouldRejectEmptyOrBlankName(String nome) {
            var request = new UpdateStudentRequestBuilder().withNome(nome).build();
            assertValidationError("nome", specWithFixedId().body(request));

        }

        @Test
        public void shouldRejectMissingAddress() {
            var request = new UpdateStudentRequestBuilder().withEndereco(null).build();
            assertValidationError("endereco", specWithFixedId().body(request));
        }
    }
}