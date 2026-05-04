package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class CreateStudentRequestBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private InstancioApi<CreateStudentRequest> api = Instancio.of(CreateStudentRequest.class)
            .generate(field(CreateStudentRequest::cpf), gen -> gen.string().digits().length(11))
            .generate(field(CreateStudentRequest::telefone), gen -> gen.string().digits().length(11))
            .supply(field(CreateStudentRequest::endereco), addressBuilder::build);


    public CreateStudentRequestBuilder withName(String name) {
        api = api.set(field(CreateStudentRequest::nome), name);
        return this;
    }

    public CreateStudentRequestBuilder withEmail(String email) {
        api = api.set(field(CreateStudentRequest::email), email);
        return this;
    }

    public CreateStudentRequestBuilder withCpf(String cpf) {
        api = api.set(field(CreateStudentRequest::cpf), cpf);
        return this;
    }

    public CreateStudentRequestBuilder withTelefone(String phone) {
        api = api.set(field(CreateStudentRequest::telefone), phone);
        return this;
    }

    public CreateStudentRequestBuilder withAddress(Address address) {
        api = api.set(field(CreateStudentRequest::endereco), address);
        return this;
    }

    public CreateStudentRequest build() {
        return api.create();
    }
}