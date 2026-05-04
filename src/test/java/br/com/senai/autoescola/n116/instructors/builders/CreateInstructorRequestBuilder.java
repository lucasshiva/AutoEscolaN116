package br.com.senai.autoescola.n116.instructors.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorRequest;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class CreateInstructorRequestBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private InstancioApi<CreateInstructorRequest> api = Instancio.of(CreateInstructorRequest.class)
            .generate(field(CreateInstructorRequest::cnh), gen -> gen.string().digits().length(10))
            .generate(field(CreateInstructorRequest::telefone), gen -> gen.string().digits().length(11))
            .supply(field(CreateInstructorRequest::endereco), addressBuilder::build);

    public CreateInstructorRequestBuilder withName(String name) {
        api = api.set(field(CreateInstructorRequest::nome), name);
        return this;
    }

    public CreateInstructorRequestBuilder withEmail(String email) {
        api = api.set(field(CreateInstructorRequest::email), email);
        return this;
    }

    public CreateInstructorRequestBuilder withCnh(String cnh) {
        api = api.set(field(CreateInstructorRequest::cnh), cnh);
        return this;
    }

    public CreateInstructorRequestBuilder withPhone(String phone) {
        api = api.set(field(CreateInstructorRequest::telefone), phone);
        return this;
    }

    public CreateInstructorRequestBuilder withAddress(Address address) {
        api = api.set(field(CreateInstructorRequest::endereco), address);
        return this;
    }

    public CreateInstructorRequest build() {
        return api.create();
    }
}