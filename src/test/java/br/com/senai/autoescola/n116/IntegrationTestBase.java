package br.com.senai.autoescola.n116;

import jakarta.annotation.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureRestTestClient
@Import(TestConfig.class)
public abstract class IntegrationTestBase {
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public RestTestClient testClient;

	private void cleanDatabase() {
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

		jdbcTemplate.execute("TRUNCATE TABLE driving_lessons");
		jdbcTemplate.execute("TRUNCATE TABLE instructors");
		jdbcTemplate.execute("TRUNCATE TABLE students");
		jdbcTemplate.execute("TRUNCATE TABLE users");

		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
	}

	@BeforeEach
	void setUp() {
		cleanDatabase();
	}

	@AfterAll
	void tearDownAll() {
		cleanDatabase();
	}

	public void assertValidationError(
			String expectedField,
			RestTestClient.RequestHeadersSpec<?> spec,
			@Nullable Consumer<ProblemDetail> extraAssertions
	) {

		spec.exchange()
			.expectStatus()
			.isBadRequest()
			.expectBody(ProblemDetail.class)
			.value(body -> {
				IO.println(body);
				assertThat(body).isNotNull();
				var properties = body.getProperties();
				assertThat(properties).isNotNull();
				assertThat(properties).isNotEmpty();

				var errors = (List<Map<String, String>>) body.getProperties().get("errors");
				assertThat(errors).isNotNull();
				assertThat(errors).hasSize(1);
				assertThat(errors).extracting(e -> e.get("field")).contains(expectedField);

				if (extraAssertions != null) {
					extraAssertions.accept(body);
				}
			});
	}

	public void assertValidationError(
			String expectedField,
			RestTestClient.RequestHeadersSpec<?> spec
	) {
		assertValidationError(expectedField, spec, null);
	}

	public <T> void assertCreatedWithTimestamp(
			RestTestClient.RequestHeadersSpec<?> spec,
			Class<T> responseType,
			Function<T, Instant> timestampExtractor,
			@Nullable Consumer<T> extraAssertions
	) {
		var response = spec.exchange();
		response
				.expectStatus().isCreated()
				.expectBody(responseType)
				.value(body -> {
					assertThat(body).isNotNull();
					assertThat(timestampExtractor.apply(body))
							.isNotNull()
							.isBefore(Instant.now())
							.isAfter(Instant.now().minusSeconds(5));

					if (extraAssertions != null) {
						extraAssertions.accept(body);
					}
				});
	}

	public <T> void assertCreatedWithTimestamp(
			RestTestClient.RequestHeadersSpec<?> spec,
			Class<T> responseType,
			Function<T, Instant> timestampExtractor
	) {
		assertCreatedWithTimestamp(spec, responseType, timestampExtractor, null);
	}

	public void assertProblemDetailWithCode(
			RestTestClient.RequestHeadersSpec<?> spec,
			HttpStatus status,
			String code
	) {
		spec.exchange()
			.expectStatus()
			.isEqualTo(status)
			.expectBody(ProblemDetail.class)
			.value(body -> {
				assertThat(body).isNotNull();
				assertThat(body.getProperties()).isNotEmpty();
				Object codeInResponse = body.getProperties().get("code");
				assertThat(codeInResponse).isInstanceOf(String.class);
				assertThat((String) codeInResponse).isNotBlank();
				assertThat(codeInResponse).isEqualTo(code);
			});
	}
}
