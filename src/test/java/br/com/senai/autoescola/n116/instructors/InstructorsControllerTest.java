package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.instructors.builders.CreateInstructorRequestBuilder;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorResponse;
import org.junit.jupiter.api.BeforeEach;
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

import static br.com.senai.autoescola.n116.utils.ControllerTestUtils.assertCreatedWithTimestamp;
import static br.com.senai.autoescola.n116.utils.ControllerTestUtils.assertValidationError;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class InstructorsControllerTest {
    @Autowired
    private InstructorsRepository instructorsRepository;

    @Autowired
    private RestTestClient testClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE instructors");
    }

    @Nested
    class CreateInstructor {
        private final CreateInstructorRequestBuilder requestBuilder =
                new CreateInstructorRequestBuilder();

        private final RestTestClient.RequestBodySpec spec = testClient.post().uri("/instructors");

        @Test
        void shouldCreateUserWithValidDTO() {
            var request = requestBuilder.build();
            assertCreatedWithTimestamp(
                    spec.body(request),
                    CreateInstructorResponse.class,
                    CreateInstructorResponse::createdAt,
                    body -> {
                        assertThat(body.nome()).isEqualTo(request.nome());
                        assertThat(instructorsRepository.findById(body.id())).isPresent();
                    }
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "123456789012345"})
        void shouldRejectInvalidCNHLength(String cnh) {
            assertValidationError("cnh", spec.body(requestBuilder.withCnh(cnh).build()));
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "123456789012345"})
        void shouldRejectInvalidPhoneLength(String phone) {
            assertValidationError("telefone", spec.body(requestBuilder.withPhone(phone).build()));
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
        void shouldRejectEmptyOrBlankName(String name) {
            assertValidationError("nome", spec.body(requestBuilder.withName(name).build()));
        }
    }

}