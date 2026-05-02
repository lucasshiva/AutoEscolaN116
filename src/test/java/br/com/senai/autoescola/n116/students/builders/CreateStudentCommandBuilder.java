package br.com.senai.autoescola.n116.students.builders;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.create.CreateStudentCommand;
import br.com.senai.autoescola.n116.utils.AddressBuilder;

public class CreateStudentCommandBuilder {
    private String name = "José Silva";
    private Address address = new AddressBuilder().build();
    private String phone = "11912341234";
    private String cpf = "12345678912";
    private String email = "josesilva@gmail.com";

    public CreateStudentCommandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CreateStudentCommandBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CreateStudentCommandBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CreateStudentCommandBuilder withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CreateStudentCommandBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CreateStudentCommand build() {
        return new CreateStudentCommand(name, email, phone, cpf, address);
    }
}