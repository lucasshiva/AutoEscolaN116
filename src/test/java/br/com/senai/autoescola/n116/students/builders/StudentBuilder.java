package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.students.Student;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.Keys;

import static org.instancio.Select.field;

public class StudentBuilder {
    private final InstancioApi<Student> api = Instancio.of(Student.class)
            .withSetting(Keys.BEAN_VALIDATION_ENABLED, true)
            .withSetting(Keys.JPA_ENABLED, true)
            .ignore(field(Student::getId))
            .ignore(field(Student::getUpdatedAt));

    public Student build() {
        return api.create();
    }
}
