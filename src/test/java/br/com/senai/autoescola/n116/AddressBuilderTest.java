package br.com.senai.autoescola.n116;

import br.com.senai.autoescola.n116.utils.AddressBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressBuilderTest {
    @Test
    @DisplayName("Test if Instancio is generating an Address correctly.")
    public void testBuild() {
        var address = new AddressBuilder().build();
        assertThat(address.getCep()).matches("\\d{8}");
        assertThat(address.getUf()).matches("[A-Z]{2}");
        assertThat(address.getCidade()).isNotBlank();
        assertThat(address.getNumero()).matches("\\d+");
        assertThat(address.getComplemento()).isNotBlank();
        assertThat(address.getLogradouro()).isNotBlank();
    }
}
