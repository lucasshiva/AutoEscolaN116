package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class StudentBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private final InstancioApi<Student> api = Instancio.of(Student.class)
            .generate(field(Student::getCpf), gen -> gen.string().digits().length(11))
            .generate(field(Student::getTelefone), gen -> gen.string().digits().length(11))
            .supply(field(Student::getEndereco), addressBuilder::build)
            .ignore(field(Student::getId))
            .ignore(field(Student::getUpdatedAt));

    public Student build() {
        return api.create();
    }
}
