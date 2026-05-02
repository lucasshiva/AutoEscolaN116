package br.com.senai.autoescola.n116.utils;

import br.com.senai.autoescola.n116.common.models.Address;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class AddressBuilder {
    private final InstancioApi<Address> api = Instancio.of(Address.class)
            .generate(field(Address::getCep), gen -> gen.string().digits().length(8))
            .generate(field(Address::getUf), gen -> gen.string().upperCase().length(2))
            .generate(field(Address::getNumero), gen -> gen.string().digits().minLength(1));

    public Address build() {
        return api.create();
    }
}
