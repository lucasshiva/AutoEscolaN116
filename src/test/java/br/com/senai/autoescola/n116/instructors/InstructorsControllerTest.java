package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.IntegrationTestBase;
import br.com.senai.autoescola.n116.instructors.builders.CreateInstructorRequestBuilder;
import br.com.senai.autoescola.n116.instructors.builders.InstructorBuilder;
import br.com.senai.autoescola.n116.instructors.builders.UpdateInstructorRequestBuilder;
import br.com.senai.autoescola.n116.instructors.create.CreateInstructorResponse;
import br.com.senai.autoescola.n116.instructors.getById.GetInstructorByIdResponse;
import br.com.senai.autoescola.n116.instructors.list.ListInstructorsResponse;
import br.com.senai.autoescola.n116.instructors.update.UpdateInstructorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InstructorsControllerTest extends IntegrationTestBase {
	@Autowired
	private InstructorsRepository instructorsRepository;

	@Nested
	class CreateInstructor {
		private final CreateInstructorRequestBuilder requestBuilder =
				new CreateInstructorRequestBuilder();

		private final RestTestClient.RequestBodySpec spec = testClient.post().uri("/instructors");

		@Test
		void shouldCreateInstructorWithValidPayload() {
			var request = requestBuilder.build();
			assertCreatedWithTimestamp(
					spec.body(request),
					CreateInstructorResponse.class,
					CreateInstructorResponse::createdAt,
					body -> {
						assertThat(body.nome()).isEqualTo(request.nome());
						assertThat(instructorsRepository.findById(body.id())).isPresent();
					}
			);
		}

		@Test
		void shouldRejectDuplicateCNH() {
			var firstInstructor = requestBuilder.build();
			var secondInstructor = requestBuilder.withCnh(firstInstructor.cnh()).build();

			spec.body(firstInstructor).exchange().expectStatus().isCreated();
			spec.body(secondInstructor).exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
		}

		@ParameterizedTest
		@ValueSource(strings = {"12345", "123456789012345"})
		void shouldRejectInvalidCNHLength(String cnh) {
			assertValidationError("cnh", spec.body(requestBuilder.withCnh(cnh).build()));
		}

		@ParameterizedTest
		@ValueSource(strings = {"12345", "123456789012345"})
		void shouldRejectInvalidPhoneLength(String phone) {
			assertValidationError("telefone", spec.body(requestBuilder.withPhone(phone).build()));
		}

		@Test
		void shouldRejectInvalidEmail() {
			assertValidationError("email", spec.body(requestBuilder.withEmail("invalidEmail").build()));
		}

		@Test
		void shouldRejectWithMissingAddress() {
			assertValidationError("endereco", spec.body(requestBuilder.withAddress(null).build()));
		}

		@ParameterizedTest
		@NullAndEmptySource
		void shouldRejectEmptyOrBlankName(String name) {
			assertValidationError("nome", spec.body(requestBuilder.withName(name).build()));
		}
	}

	@Nested
	class GetInstructor {
		@Test
		public void shouldReturnCorrectInstructor() {
			var builder = new InstructorBuilder();
			Instructor instructor = builder.build();
			instructorsRepository.save(instructor);

			testClient.get().uri("/instructors/{id}", instructor.getId()).exchange()
					  .expectStatus().isOk()
					  .expectBody(GetInstructorByIdResponse.class)
					  .value(body -> {
						  assertThat(body).isNotNull();
						  assertThat(body.id()).isEqualTo(instructor.getId());
					  });
		}

		@Test
		public void shouldNotFindInstructor() {
			testClient.get().uri("/instructors/{id}", 1).exchange().expectStatus().isNotFound();
		}
	}

	@Nested
	class DeleteInstructor {
		@Test
		@DisplayName("SoftDelete works automatically via Hibernate.")
		public void shouldSoftDelete() {
			var instructor = new InstructorBuilder().build();

			var saved = instructorsRepository.save(instructor);

			var response = testClient.delete().uri("/instructors/{id}", saved.getId()).exchange();
			response.expectStatus().isNoContent();

			// Hibernate filters won't apply here - raw SQL sees everything
			var deletedAt = jdbcTemplate.queryForObject(
					"SELECT deleted_at FROM instructors WHERE id = ?",
					Instant.class,
					saved.getId()
			);

			// Hibernate sees nothing - it manages soft deletion automatically.
			assertThat(instructorsRepository.count()).isZero();

			// But the row is still there.
			assertThat(deletedAt).isNotNull();
		}

		@Test
		@DisplayName("Returns a 404 Not Found when the ID doesn't exist.")
		public void missingId() {
			var response = testClient.delete().uri("/instructors/{id}", 1).exchange();
			response.expectStatus().isNotFound();
		}
	}

	@Nested
	class ListInstructors {
		@Test
		public void emptyList() {
			testClient.get().uri("/instructors").exchange()
					  .expectStatus().isOk()
					  .expectBody(ListInstructorsResponse.class)
					  .value(body -> {
						  assertThat(body).isNotNull();
						  assertThat(body.data()).isEmpty();
						  assertThat(body.totalPages()).isZero();
						  assertThat(body.totalElements()).isZero();
					  });
		}

		@Test
		public void shouldReturnAllInstructors() {
			var builder = new InstructorBuilder();
			instructorsRepository.saveAll(List.of(
					builder.build(),
					builder.build(),
					builder.build()
			));

			testClient.get().uri("/instructors").exchange()
					  .expectStatus().isOk()
					  .expectBody(ListInstructorsResponse.class)
					  .value(body -> {
						  assertThat(body).isNotNull();
						  assertThat(body.data()).hasSize(3);
						  assertThat(body.totalPages()).isOne();
						  assertThat(body.totalElements()).isEqualTo(3);
					  });
		}

		@Test
		void shouldNotIncludeDeletedInstructors() {
			var builder = new InstructorBuilder();
			Instructor first = builder.build();
			Instructor second = builder.build();

			instructorsRepository.saveAll(List.of(
					first,
					second
			));

			// Delete first instructor
			testClient.delete().uri("/instructors/{id}", first.getId()).exchange();

			// Ensure we only have one instructor in the response
			testClient.get().uri("/instructors").exchange()
					  .expectStatus().isOk()
					  .expectBody(ListInstructorsResponse.class)
					  .value(body -> {
						  assertThat(body).isNotNull();
						  assertThat(body.data()).hasSize(1);
						  assertThat(body.totalElements()).isEqualTo(1);
					  });
		}

		@Test
		void shouldReturnCorrectPaginationMetadata() {
			var builder = new InstructorBuilder();
			instructorsRepository.saveAll(
					Stream.generate(builder::build)
						  .limit(15)
						  .toList()
			);

			testClient.get().uri("/instructors?page=1&size=10").exchange()
					  .expectStatus().isOk()
					  .expectBody(ListInstructorsResponse.class)
					  .value(body -> {
						  assertThat(body).isNotNull();
						  assertThat(body.data()).hasSize(10);
						  assertThat(body.totalElements()).isEqualTo(15);
						  assertThat(body.totalPages()).isEqualTo(2);
						  assertThat(body.page()).isOne();
						  assertThat(body.size()).isEqualTo(10);
					  });
		}
	}

	@Nested
	class EditInstructor {
		private final RestTestClient.RequestBodySpec specWithFixedId = testClient.put().uri("/instructors/{id}", 1);

		@Test
		public void shouldEditInstructor() {
			var instructor = new InstructorBuilder().build();
			var request = new UpdateInstructorRequestBuilder().build();

			instructorsRepository.save(instructor);
			var response = testClient.put().uri("/instructors/{id}", instructor.getId()).body(request).exchange();
			response.expectStatus().isOk();

			response.expectBody(UpdateInstructorResponse.class).value(body -> {
				assertThat(body).isNotNull();
				// Check if we're returning the body correctly.
				assertThat(request)
						.usingRecursiveComparison()
						.comparingOnlyFields("nome", "telefone", "endereco")
						.isEqualTo(body);

				// Check if we're persisting changes correctly.
				var opt = instructorsRepository.findById(instructor.getId());
				if (opt.isEmpty())
					throw new RuntimeException("Could not find instructor in repository");

				var persisted = opt.get();
				assertThat(request)
						.usingRecursiveComparison()
						.comparingOnlyFields("nome", "telefone", "endereco")
						.isEqualTo(persisted);
			});
		}


		@ParameterizedTest
		@ValueSource(strings = {"123", "1232132144242"})
		public void shouldRejectTelefoneWithWrongLength(String telefone) {
			var request = new UpdateInstructorRequestBuilder().withTelefone(telefone).build();
			assertValidationError("telefone", specWithFixedId.body(request));
		}

		@ParameterizedTest
		@NullAndEmptySource
		public void shouldRejectEmptyOrBlankName(String nome) {
			var request = new UpdateInstructorRequestBuilder().withNome(nome).build();
			assertValidationError("nome", specWithFixedId.body(request));

		}

		@Test
		public void shouldRejectMissingAddress() {
			var request = new UpdateInstructorRequestBuilder().withEndereco(null).build();
			assertValidationError("endereco", specWithFixedId.body(request));
		}
	}
}