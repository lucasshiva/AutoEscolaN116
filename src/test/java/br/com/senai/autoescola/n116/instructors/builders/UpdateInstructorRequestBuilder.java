package br.com.senai.autoescola.n116.instructors.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.instructors.update.UpdateInstructorRequest;
import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class UpdateInstructorRequestBuilder {
    private static final AddressBuilder addressBuilder = new AddressBuilder();

    private InstancioApi<UpdateInstructorRequest> api = Instancio.of(UpdateInstructorRequest.class)
            .generate(field(UpdateInstructorRequest::telefone), gen -> gen.string().digits().length(11))
            .supply(field(UpdateInstructorRequest::endereco), addressBuilder::build);

    public UpdateInstructorRequest build() {
        return api.create();
    }

    public UpdateInstructorRequestBuilder withTelefone(String telefone) {
        api = api.set(field(UpdateInstructorRequest::telefone), telefone);
        return this;
    }

    public UpdateInstructorRequestBuilder withNome(String nome) {
        api = api.set(field(UpdateInstructorRequest::nome), nome);
        return this;
    }

    public UpdateInstructorRequestBuilder withEndereco(Address endereco) {
        api = api.set(field(UpdateInstructorRequest::endereco), endereco);
        return this;
    }
}
