package br.com.senai.autoescola.n116.lessons.actions.schedule;

import br.com.senai.autoescola.n116.common.models.Especialidade;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.LocalDate;
import java.time.LocalTime;

@With
public record ScheduleLessonRequest(
		@NotNull
		Long studentId,

		@Nullable
		Long instructorId,

		@NotNull
		Especialidade category,

		@NotNull
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate date,

		@NotNull
		@JsonFormat(pattern = "HH:mm")
		LocalTime time
) {
}
