package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.IntegrationTestBase;
import br.com.senai.autoescola.n116.common.models.Especialidade;
import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.instructors.InstructorNotFoundException;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import br.com.senai.autoescola.n116.instructors.builders.InstructorBuilder;
import br.com.senai.autoescola.n116.lessons.actions.schedule.ScheduleLessonHandler;
import br.com.senai.autoescola.n116.lessons.actions.schedule.ScheduleLessonRequest;
import br.com.senai.autoescola.n116.lessons.actions.schedule.ScheduleLessonResponse;
import br.com.senai.autoescola.n116.lessons.actions.schedule.exceptions.*;
import br.com.senai.autoescola.n116.lessons.messaging.LessonRabbitProperties;
import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import br.com.senai.autoescola.n116.students.builders.StudentBuilder;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;
import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;

class LessonsControllerTest extends IntegrationTestBase {

	private final InstructorBuilder instructorBuilder = new InstructorBuilder();
	private final StudentBuilder studentBuilder = new StudentBuilder();

	@Autowired
	private InstructorsRepository instructorsRepository;

	@Autowired
	private StudentsRepository studentsRepository;

	@Autowired
	private DrivingLessonsRepository lessonsRepository;

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Autowired
	private LessonRabbitProperties rabbitProperties;

	public Callable<Boolean> isQueueEmpty(String queue) {
		return () -> Objects.requireNonNull(rabbitAdmin.getQueueInfo(queue)).getMessageCount() == 0;
	}

	@Nested
	class ScheduleLessonTest {
		RestTestClient.RequestBodySpec spec = testClient.post().uri("/lessons");

		@BeforeEach
		void setup() {
			instructorsRepository.save(validInstructor);
			studentsRepository.save(validStudent);
			rabbitAdmin.purgeQueue(rabbitProperties.queue(), true);
		}

		ScheduleLessonRequest validRequest = new ScheduleLessonRequest(
				1L,
				null,
				Especialidade.MOTOS,
				LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
				ScheduleLessonHandler.MIN_SCHEDULING_HOUR
		);
		Instructor validInstructor = instructorBuilder.build().withEspecialidade(validRequest.category());
		Student validStudent = studentBuilder.build();

		DrivingLesson validLesson = new DrivingLesson(
				null,
				validStudent,
				validInstructor,
				new LessonSchedule(
						validRequest.date(),
						validRequest.time(),
						validRequest.time().plus(ScheduleLessonHandler.CLASS_DURATION)
				),
				LessonStatus.SCHEDULED,
				null,
				null,
				null,
				null
		);


		@Test
		public void shouldRejectSchedulingOnSundays() {
			var nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
			var request = validRequest
					.withDate(nextSunday)
					.withInstructorId(validInstructor.getId())
					.withStudentId(validStudent.getId());

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleOnSundaysException.code
			);
		}

		@ParameterizedTest
		@ValueSource(ints = {5, 21})
		public void shouldRejectSchedulingLessonsOutsideWorkingHours(int hour) {
			var invalidHour = LocalTime.of(hour, 0);
			var request = validRequest
					.withTime(invalidHour)
					.withInstructorId(validInstructor.getId())
					.withStudentId(validStudent.getId());

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleOutsideOfWorkingHoursException.code
			);
		}

		@Test
		public void shouldNotScheduleBeforeMinimumAdvanceTime() {
			// current time: 14:40 (2:40 PM) - based on Clock configuration and America/Sao_Paulo timezone.
			// try to schedule lesson at 3 PM - only 20 minutes in advance.

			var friday = LocalDate.of(2026, 5, 8);
			var threePM = LocalTime.of(15, 0);

			var request = validRequest
					.withDate(friday)
					.withTime(threePM)
					.withStudentId(validStudent.getId())
					.withInstructorId(validInstructor.getId());

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleMinimumAdvanceException.code
			);
		}

		@Test
		public void shouldScheduleAfterMinimumAdvanceTime() {
			// current time: 14:40 (2:40 PM) - based on Clock configuration and America/Sao_Paulo timezone.
			// schedule lesson at 4 PM

			var friday = LocalDate.of(2026, 5, 8);
			var fourPM = LocalTime.of(16, 0);

			var request = validRequest
					.withDate(friday)
					.withTime(fourPM)
					.withStudentId(validStudent.getId())
					.withInstructorId(validInstructor.getId());

			spec.body(request)
				.exchange()
				.expectStatus()
				.isCreated();
		}

		@Test
		public void shouldSendScheduledMessage() {
			// TODO: find a way to retrieve the specific event from a rabbitmq queue.
			spec.body(validRequest).exchange().expectStatus().isCreated();
			var foundMessage = Awaitility
					.await()
					.atMost(Duration.ofSeconds(5))
					.until(isQueueEmpty(rabbitProperties.queue()), Boolean.TRUE::equals);

			assertThat(foundMessage).isTrue();
		}

		@Test
		public void shouldOnlyScheduleLessonsOnFullHours() {
			var hour = LocalTime.of(9, 30);
			var request = validRequest
					.withStudentId(validStudent.getId())
					.withInstructorId(validInstructor.getId())
					.withTime(hour);

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleFullHourException.code
			);
		}

		@Test
		public void shouldNotScheduleWhenStudentAlreadyHasALessonOnTheSameDay() {
			var secondLessonRequest = validRequest.withTime(validRequest.time().plusHours(2));

			// Schedule first lesson.
			spec.body(validRequest).exchange().expectStatus().isCreated();

			// Schedule second lesson, same day but a few hours later.
			assertProblemDetailWithCode(
					spec.body(secondLessonRequest),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleTooManyLessonsOnTheSameDayException.code
			);
		}

		@Test
		public void shouldScheduleWithChosenInstructorWhenAvailable() {
			var request = validRequest.withInstructorId(validInstructor.getId());
			spec.body(request).exchange().expectStatus().isCreated();
		}

		@Test
		public void shouldNotScheduleWhenChosenInstructorIsNotAvailable() {
			var request = validRequest.withInstructorId(validInstructor.getId());
			// Schedule a lesson with the instructor.
			spec.body(request).exchange().expectStatus().isCreated();

			// Try to do it again, but for another student.
			var anotherStudent = studentBuilder.build();
			var savedStudent = studentsRepository.save(anotherStudent);
			var anotherRequest = request.withStudentId(savedStudent.getId());

			assertProblemDetailWithCode(
					spec.body(anotherRequest),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleInstructorNotAvailableException.code
			);
		}

		@Test
		public void shouldScheduleWithAnyAvailableInstructor() {
			var request = validRequest.withStudentId(validStudent.getId()).withInstructorId(null);
			var response = spec
					.body(request)
					.exchange()
					.expectStatus()
					.isCreated()
					.expectBody(ScheduleLessonResponse.class)
					.returnResult()
					.getResponseBody();

			assertThat(response).isNotNull();
			var lessonInDb = lessonsRepository.findById(response.lessonId());
			assertThat(lessonInDb).isPresent();
			assertThat(lessonInDb.get().getInstructor().getId()).isEqualTo(validInstructor.getId());
		}

		@Test
		public void shouldNotScheduleWhenThereAnyNoAvailableInstructors() {
			lessonsRepository.save(validLesson);

			var anotherStudent = studentsRepository.save(validStudent.withId(null).withCpf("12345678910"));
			var request = validRequest.withStudentId(anotherStudent.getId()).withInstructorId(null);

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.UNPROCESSABLE_CONTENT,
					ScheduleNoInstructorsAvailableException.code
			);
		}

		@Test
		public void shouldFailWhenStudentIsNotFound() {
			var missingStudent = validStudent.withId(validStudent.getId() + 1L);
			var request = validRequest
					.withStudentId(missingStudent.getId())
					.withInstructorId(validInstructor.getId());

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.NOT_FOUND,
					StudentNotFoundException.code
			);
		}

		@Test
		public void shouldFailWhenInstructorIsNotFound() {
			var invalidInstructor = validInstructor.withId(validInstructor.getId() + 1L);
			var request = validRequest
					.withInstructorId(invalidInstructor.getId())
					.withStudentId(validStudent.getId());

			assertProblemDetailWithCode(
					spec.body(request),
					HttpStatus.NOT_FOUND,
					InstructorNotFoundException.code
			);
		}
	}
}
