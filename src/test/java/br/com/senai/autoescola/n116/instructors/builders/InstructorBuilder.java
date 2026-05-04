package br.com.senai.autoescola.n116.instructors.builders;

import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class InstructorBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private final InstancioApi<Instructor> api = Instancio.of(Instructor.class)
            .generate(field(Instructor::getCnh), gen -> gen.string().digits().length(10))
            .generate(field(Instructor::getTelefone), gen -> gen.string().digits().length(11))
            .supply(field(Instructor::getEndereco), addressBuilder::build)
            .ignore(field(Instructor::getId))
            .ignore(field(Instructor::getUpdatedAt));

    public Instructor build() {
        return api.create();
    }
}
