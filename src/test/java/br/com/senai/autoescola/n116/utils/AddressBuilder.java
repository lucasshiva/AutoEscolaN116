package br.com.senai.autoescola.n116.utils;

import br.com.senai.autoescola.n116.common.models.Address;

public class AddressBuilder {
    private final String logradouro = "Rua Passos";
    private final String numero = "650";
    private final String complemento = "Casa 2";
    private final String cidade = "Vila Nova";
    private final String cep = "05060789";
    private final String uf = "SP";

    public Address build() {
        return new Address(logradouro, numero, complemento, cidade, cep, uf);
    }
}
