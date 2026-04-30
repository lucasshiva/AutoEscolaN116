package br.com.senai.autoescola.n116;

import br.com.senai.autoescola.n116.students.StudentsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AutoEscolaN116ApplicationTests {

    @Autowired
    private StudentsController studentsController;

    @Test
    void contextLoads() {
        assertThat(studentsController).isNotNull();
    }
}
