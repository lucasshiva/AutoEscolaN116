package br.com.senai.autoescola.n116;

import br.com.senai.autoescola.n116.auth.AuthController;
import br.com.senai.autoescola.n116.instructors.InstructorsController;
import br.com.senai.autoescola.n116.students.StudentsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AutoEscolaN116ApplicationTests {

    @Autowired
    private StudentsController studentsController;

    @Autowired
    private InstructorsController instructorsController;

    @Autowired
    private AuthController authController;

    @Test
    void contextLoads() {
        assertThat(studentsController).isNotNull();
        assertThat(instructorsController).isNotNull();
        assertThat(authController).isNotNull();
    }
}
