package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.create.CreateStudentCommand;
import br.com.senai.autoescola.n116.students.create.CreateStudentResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ImportTestcontainers(TestcontainersConfiguration.class)
class StudentsControllerTest {
    private static final Address endereco = new Address(
            "Rua",
            null,
            null,
            "Cidade",
            "cep",
            "uf"
    );
    static MySQLContainer mysql = new MySQLContainer("mysql:8.1").withReuse(true);
    @Autowired
    private RestTestClient testClient;
    @Autowired
    private StudentsRepository studentsRepository;

    private static Stream<CreateStudentCommand> invalidCommands() {
        return Stream.of(
                // Empty fields.
                new CreateStudentCommand("", "", "", "", endereco),

                // Missing address
                new CreateStudentCommand("Hey", "hey@gmail.com", "", "", null),

                // Phone number too big.
                new CreateStudentCommand("name", "name@gmail.com", "12345678912345", "12345678911", endereco),

                // CPF too big
                new CreateStudentCommand("name", "name@gmail.com", "12312341234", "12345678911235", endereco)
        );
    }

    @DynamicPropertySource
    static void mySqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    @BeforeEach
    void setUp() {
        studentsRepository.deleteAll();
    }

    @Test
    void shouldCreateUserWithValidDTO() {
        // Arrange
        var command = new CreateStudentCommand(
                "José Silva",
                "josesilva@gmail.com",
                "11912341234",
                "12345678912",
                endereco
        );

        // Act
        var result = testClient.post().uri("/students").body(command).exchange();
        var allStudents = studentsRepository.findAll();

        // Assert
        result.expectStatus().isCreated();
        assertThat(allStudents.size()).isEqualTo(1);
        result
                .expectBody(CreateStudentResponse.class)
                .value(body -> {
                    assertThat(body).isNotNull();
                    assertThat(body.createdAt()).isNotNull();
                    assertThat(body.id()).isEqualTo(1L);
                });

    }

    @ParameterizedTest
    @MethodSource("invalidCommands")
    void shouldRejectInvalidDTO(CreateStudentCommand cmd) {
        var response = testClient.post().uri("/students").body(cmd).exchange();
        response.expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }
}