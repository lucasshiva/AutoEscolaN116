package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.create.CreateStudentRequest;
import br.com.senai.autoescola.n116.utils.AddressBuilder;

public class CreateStudentRequestBuilder {
    private String name = "José Silva";
    private Address address = new AddressBuilder().build();
    private String phone = "11912341234";
    private String cpf = "12345678912";
    private String email = "josesilva@gmail.com";

    public CreateStudentRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CreateStudentRequestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CreateStudentRequestBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CreateStudentRequestBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CreateStudentRequestBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CreateStudentRequest build() {
        return new CreateStudentRequest(name, email, phone, cpf, address);
    }
}