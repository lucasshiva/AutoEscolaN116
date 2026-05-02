package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.Keys;

import static org.instancio.Select.field;

public class CreateStudentRequestBuilder {
    private InstancioApi<CreateStudentRequest> api = Instancio.of(CreateStudentRequest.class)
            .withSetting(Keys.BEAN_VALIDATION_ENABLED, true)
            .withSetting(Keys.JPA_ENABLED, true);

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

    public CreateStudentRequestBuilder withPhone(String phone) {
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