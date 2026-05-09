package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.IntegrationTestBase;
import br.com.senai.autoescola.n116.common.models.Especialidade;
import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import br.com.senai.autoescola.n116.instructors.builders.InstructorBuilder;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonHandler;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonRequest;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonResponse;
import br.com.senai.autoescola.n116.lessons.schedule.exceptions.ScheduleInvalidDayException;
import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import br.com.senai.autoescola.n116.students.builders.StudentBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

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

	@Nested
	class ScheduleLessonTest {
		RestTestClient.RequestBodySpec spec = testClient.post().uri("/lessons");

		@BeforeEach
		void setup() {
			instructorsRepository.save(validInstructor);
			studentsRepository.save(validStudent);
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

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo(ScheduleInvalidDayException.message);
		}

		@ParameterizedTest
		@ValueSource(ints = {5, 21})
		public void shouldRejectSchedulingLessonsOutsideWorkingHours(int hour) {
			var invalidHour = LocalTime.of(hour, 0);
			var request = validRequest
					.withTime(invalidHour)
					.withInstructorId(validInstructor.getId())
					.withStudentId(validStudent.getId());

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("Invalid hour '" + invalidHour + "' for schedule time 06:00 ~ 20:00");
		}

		@Test
		public void shouldNotScheduleBeforeMinimumAdvanceTime() {
			// current time: 14:40 (2:40 PM) - based on Clock configuration.
			// try to schedule lesson at 3 PM - only 20 minutes in advance.

			var friday = LocalDate.of(2026, 5, 8);
			var threePM = LocalTime.of(15, 0);

			var request = validRequest
					.withDate(friday)
					.withTime(threePM)
					.withStudentId(validStudent.getId())
					.withInstructorId(validInstructor.getId());

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("You can only schedule lessons with a minimum of " + ScheduleLessonHandler.MIN_ADVANCE.toMinutes() + " minutes in advance");
		}

		@Test
		public void shouldScheduleAfterMinimumAdvanceTime() {
			// current time: 14:40 (2:40 PM) - based on Clock configuration.
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
		public void shouldOnlyScheduleLessonsOnFullHours() {
			var hour = LocalTime.of(9, 30);
			var request = validRequest
					.withStudentId(validStudent.getId())
					.withInstructorId(validInstructor.getId())
					.withTime(hour);

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("Lessons can only be scheduled in full hours (e.g 8AM, 9AM, etc.)");
		}

		@Test
		public void shouldNotScheduleWhenStudentAlreadyHasALessonOnTheSameDay() {
			var secondLessonRequest = validRequest.withTime(validRequest.time().plusHours(2));

			// Schedule first lesson.
			spec.body(validRequest).exchange().expectStatus().isCreated();

			// Schedule second lesson, same day but a few hours later.
			spec.body(secondLessonRequest)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("Students cannot have more than one lesson per day");
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
			var anotherStudent = validStudent.withId(2L);
			spec.body(request.withStudentId(anotherStudent.getId())).exchange().expectStatus().isNotFound();
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

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("There are no instructors available for this driving lesson");
		}

		@Test
		public void shouldFailWhenStudentIsNotFound() {
			var missingStudent = validStudent.withId(validStudent.getId() + 1L);
			var request = validRequest
					.withStudentId(missingStudent.getId())
					.withInstructorId(validInstructor.getId());

			spec.body(request)
				.exchange()
				.expectStatus()
				.isNotFound();
		}

		@Test
		public void shouldFailWhenInstructorIsNotFound() {
			var invalidInstructor = validInstructor.withId(validInstructor.getId() + 1L);
			var request = validRequest
					.withInstructorId(invalidInstructor.getId())
					.withStudentId(validStudent.getId());
			spec.body(request).exchange().expectStatus().isNotFound();
		}
	}
}
