package br.com.senai.autoescola.n116.lessons.schedule;

import br.com.senai.autoescola.n116.common.models.Especialidade;
import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.instructors.InstructorNotFoundException;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import br.com.senai.autoescola.n116.lessons.DrivingLesson;
import br.com.senai.autoescola.n116.lessons.DrivingLessonsRepository;
import br.com.senai.autoescola.n116.lessons.LessonSchedule;
import br.com.senai.autoescola.n116.lessons.LessonStatus;
import br.com.senai.autoescola.n116.lessons.schedule.exceptions.*;
import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

@Component
public class ScheduleLessonHandler {
	public static LocalTime MIN_SCHEDULING_HOUR = LocalTime.of(6, 0);
	public static LocalTime MAX_SCHEDULING_HOUR = LocalTime.of(20, 0);
	public static Duration MIN_ADVANCE = Duration.ofMinutes(30);
	public static Duration CLASS_DURATION = Duration.ofMinutes(60);
	public static Set<DayOfWeek> WORKING_DAYS = new HashSet<>(EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.SATURDAY));

	private final StudentsRepository studentsRepository;
	private final InstructorsRepository instructorsRepository;
	private final DrivingLessonsRepository lessonsRepository;

	public ScheduleLessonHandler(
			StudentsRepository studentsRepository,
			InstructorsRepository instructorsRepository,
			DrivingLessonsRepository lessonsRepository
	) {
		this.studentsRepository = studentsRepository;
		this.instructorsRepository = instructorsRepository;
		this.lessonsRepository = lessonsRepository;
	}

	public ScheduleLessonResponse schedule(ScheduleLessonRequest request) {
		DayOfWeek day = request.date().getDayOfWeek();
		if (!WORKING_DAYS.contains(day)) {
			throw new ScheduleInvalidDayException();
		}

		if (request.time().isBefore(MIN_SCHEDULING_HOUR) || request.time().isAfter(MAX_SCHEDULING_HOUR)) {
			throw new ScheduleInvalidHourException(request.time(), MIN_SCHEDULING_HOUR, MAX_SCHEDULING_HOUR);
		}

		// Check if the scheduled class is at least 30 minutes before current time
		var scheduled = LocalDateTime.of(request.date(), request.time());
		var now = LocalDateTime.now();
		if (scheduled.isBefore(now.plus(MIN_ADVANCE))) {
			throw new ScheduleTooSoonException(MIN_ADVANCE.toMinutes());
		}

		// Lessons can only happen on full hours
		if (request.time().getMinute() > 0) {
			throw new ScheduleFullHourException();
		}

		// Student cannot have two lessons on the same day
		if (lessonsRepository.studentAlreadyHasLessonOnDate(request.studentId(), request.date())) {
			throw new ScheduleTooManyLessonsException();
		}

		LessonSchedule schedule = new LessonSchedule(
				request.date(),
				request.time(),
				request.time().plusMinutes(CLASS_DURATION.toMinutes())
		);

		Student student = studentsRepository
				.findById(request.studentId())
				.orElseThrow(() -> new StudentNotFoundException(request.studentId()));

		Instructor instructor = chooseInstructor(request.instructorId(), schedule, request.category());

		var lesson = createDrivingLesson(student, instructor, schedule);
		var saved = lessonsRepository.save(lesson);
		return new ScheduleLessonResponse(
				saved.getId(),
				student.getId(),
				instructor.getId(),
				instructor.getEspecialidade(),
				saved.getSchedule().getDate(),
				saved.getSchedule().getStartHour(),
				saved.getSchedule().getEndHour()
		);
	}

	private Instructor chooseInstructor(Long instructorId, LessonSchedule schedule, Especialidade category) {
		if (instructorId==null) {
			// Select any available for category.
			var availableInstructors = lessonsRepository.findAvailableInstructors(
					category,
					schedule.getStartHour(),
					schedule.getEndHour()
			);
		}

		var instructor = instructorsRepository
				.findById(instructorId)
				.orElseThrow(() -> new InstructorNotFoundException(instructorId));

		boolean isAvailable = lessonsRepository.instructorIsAvailable(
				instructor.getId(),
				schedule.getStartHour(),
				schedule.getEndHour()
		);

		if (!isAvailable) {
			throw new ScheduleInstructorNotAvailableException(instructor.getId());
		}

		return instructor;
	}

	private DrivingLesson createDrivingLesson(Student student, Instructor instructor, LessonSchedule schedule) {
		return new DrivingLesson(
				null,
				student,
				instructor,
				schedule,
				LessonStatus.SCHEDULED,
				null,
				null,
				null,
				null
		);
	}
}
