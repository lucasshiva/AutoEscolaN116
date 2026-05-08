package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.IntegrationTestBase;
import br.com.senai.autoescola.n116.common.models.Especialidade;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import br.com.senai.autoescola.n116.instructors.builders.InstructorBuilder;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonHandler;
import br.com.senai.autoescola.n116.lessons.schedule.ScheduleLessonRequest;
import br.com.senai.autoescola.n116.lessons.schedule.exceptions.ScheduleInvalidDayException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import br.com.senai.autoescola.n116.students.builders.StudentBuilder;
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

class LessonsControllerTest extends IntegrationTestBase {
	private final InstructorBuilder instructorBuilder = new InstructorBuilder();
	private final StudentBuilder studentBuilder = new StudentBuilder();

	@Autowired
	private InstructorsRepository instructorsRepository;

	@Autowired
	private StudentsRepository studentsRepository;

	@Nested
	class ScheduleLesson {
		RestTestClient.RequestBodySpec spec = testClient.post().uri("/lessons");

		ScheduleLessonRequest validRequest = new ScheduleLessonRequest(
				1L,
				1L,
				Especialidade.MOTOS,
				LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
				ScheduleLessonHandler.MIN_SCHEDULING_HOUR
		);

		@Test
		public void shouldRejectSchedulingOnSundays() {
			var instructor = instructorBuilder.build();
			instructorsRepository.save(instructor);

			var student = studentBuilder.build();
			studentsRepository.save(student);

			var nextSunday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
			var request = validRequest
					.withDate(nextSunday)
					.withInstructorId(instructor.getId())
					.withStudentId(student.getId());

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
			var instructor = instructorBuilder.build();
			instructorsRepository.save(instructor);

			var student = studentBuilder.build();
			studentsRepository.save(student);

			var invalidHour = LocalTime.of(hour, 0);
			var request = validRequest
					.withTime(invalidHour)
					.withInstructorId(instructor.getId())
					.withStudentId(student.getId());

			spec.body(request)
				.exchange()
				.expectStatus()
				.isBadRequest()
				.expectBody()
				.jsonPath("$.message")
				.isEqualTo("Invalid hour '" + invalidHour + "' for schedule time 06:00 ~ 20:00");
		}
	}
}
