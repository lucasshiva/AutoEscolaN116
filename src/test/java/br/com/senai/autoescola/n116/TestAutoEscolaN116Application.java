package br.com.senai.autoescola.n116;

import org.springframework.boot.SpringApplication;

public class TestAutoEscolaN116Application {

    public static void main(String[] args) {
        SpringApplication.from(AutoEscolaN116Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
