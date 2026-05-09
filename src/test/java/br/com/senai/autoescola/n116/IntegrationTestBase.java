package br.com.senai.autoescola.n116;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

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
}
