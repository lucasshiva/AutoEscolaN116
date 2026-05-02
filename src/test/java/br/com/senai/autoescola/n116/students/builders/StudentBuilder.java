package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.students.Student;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class StudentBuilder {
    private final InstancioApi<Student> api = Instancio.of(Student.class)
            .generate(field(Student::getCpf), gen -> gen.string().digits().length(11))
            .generate(field(Student::getTelefone), gen -> gen.string().digits().length(11))
            .ignore(field(Student::getId))
            .ignore(field(Student::getUpdatedAt));

    public Student build() {
        return api.create();
    }
}
