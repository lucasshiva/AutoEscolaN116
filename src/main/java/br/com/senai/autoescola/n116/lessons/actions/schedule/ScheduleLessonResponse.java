package br.com.senai.autoescola.n116.lessons.actions.schedule;

import br.com.senai.autoescola.n116.common.models.Especialidade;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleLessonResponse(
		Long lessonId,
		Long studentId,
		Long instructorId,
		Especialidade category,
		LocalDate date,
		LocalTime hourStart,
		LocalTime hourEnd
) {
}