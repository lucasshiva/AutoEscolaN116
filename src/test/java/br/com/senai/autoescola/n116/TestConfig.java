package br.com.senai.autoescola.n116;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
public class TestConfig {
	@Bean
	@Primary
	Clock fixedClock() {
		return Clock.fixed(
				Instant.parse("2026-05-08T17:40:00Z"),
				ZoneId.of("America/Sao_Paulo")
		);
	}
}
