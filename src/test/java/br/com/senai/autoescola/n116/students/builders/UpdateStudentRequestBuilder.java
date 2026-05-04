package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.update.UpdateStudentRequest;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class UpdateStudentRequestBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private InstancioApi<UpdateStudentRequest> api = Instancio.of(UpdateStudentRequest.class)
            .generate(field(UpdateStudentRequest::telefone), gen -> gen.string().digits().length(11))
            .supply(field(UpdateStudentRequest::endereco), addressBuilder::build);

    public UpdateStudentRequest build() {
        return api.create();
    }

    public UpdateStudentRequestBuilder withTelefone(String telefone) {
        api = api.set(field(UpdateStudentRequest::telefone), telefone);
        return this;
    }

    public UpdateStudentRequestBuilder withNome(String nome) {
        api = api.set(field(UpdateStudentRequest::nome), nome);
        return this;
    }

    public UpdateStudentRequestBuilder withEndereco(Address endereco) {
        api = api.set(field(UpdateStudentRequest::endereco), endereco);
        return this;
    }
}
