package br.com.senai.autoescola.n116.students.factories;

import br.com.senai.autoescola.n116.students.Student;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.Keys;

import static org.instancio.Select.field;

public class StudentFactory {
    private static InstancioApi<Student> base() {
        return Instancio.of(Student.class)
                .withSetting(Keys.BEAN_VALIDATION_ENABLED, true)
                .withSetting(Keys.JPA_ENABLED, true)
                .ignore(field(Student::getId))
                .ignore(field(Student::getUpdatedAt));
    }

    public static Student valid() {
        return base().create();
    }
}
