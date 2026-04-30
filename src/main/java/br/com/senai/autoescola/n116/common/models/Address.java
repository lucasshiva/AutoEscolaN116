package br.com.senai.autoescola.n116.common.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Column(name = "endereco_logradouro")
    @NotBlank
    private String logradouro;

    @Column(name = "endereco_numero")
    private String numero;

    @Column(name = "endereco_complemento")
    private String complemento;

    @Column(name = "endereco_cidade")
    @NotBlank
    private String cidade;

    @Column(name = "endereco_cep", unique = true)
    @NotBlank
    private String cep;

    @Column(name = "endereco_uf")
    @NotBlank
    private String uf;
}
