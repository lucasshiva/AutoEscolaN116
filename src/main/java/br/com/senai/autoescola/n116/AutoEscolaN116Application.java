package br.com.senai.autoescola.n116;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
//@EnableConfigurationProperties(LessonsRabbitConfig.class)
public class AutoEscolaN116Application {

	static void main(String[] args) {
		SpringApplication.run(AutoEscolaN116Application.class, args);
	}
}