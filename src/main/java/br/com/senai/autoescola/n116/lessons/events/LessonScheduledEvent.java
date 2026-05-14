package br.com.senai.autoescola.n116.lessons.events;

import java.time.LocalDateTime;

public record LessonScheduledEvent(
		Long lessonId,
		String studentName,
		String studentEmail,
		String instructorName,
		String instructorEmail,
		LocalDateTime date
) {
}
