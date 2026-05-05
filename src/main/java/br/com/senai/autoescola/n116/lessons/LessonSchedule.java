package br.com.senai.autoescola.n116.lessons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LessonSchedule {
    @Column(name = "schedule_date")
    private LocalDate date;

    @Column(name = "schedule_start")
    private LocalTime startHour;

    @Column(name = "schedule_end")
    private LocalTime endHour;
}
