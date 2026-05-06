package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.common.models.Especialidade;
import br.com.senai.autoescola.n116.instructors.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DrivingLessonsRepository extends JpaRepository<DrivingLesson, Long> {

    @Query("""
                SELECT COUNT(l) > 0
                FROM DrivingLesson l
                WHERE l.student.id = :studentId
                  AND l.schedule.date = :date
            """)
    boolean studentAlreadyHasLessonOnDate(Long studentId, LocalDate date);


    @Query("""
                SELECT COUNT(l) = 0
                FROM DrivingLesson l
                WHERE l.instructor.id = :instructorId
                  AND l.schedule.startHour < :end
                  AND l.schedule.endHour > :start
            """)
    boolean instructorIsAvailable(
            Long instructorId,
            LocalTime start,
            LocalTime end
    );

    @Query("""
                SELECT i
                FROM Instructor i
                WHERE i.especialidade = :category
                  AND NOT EXISTS (
                      SELECT l FROM DrivingLesson l
                      WHERE l.instructor = i
                        AND l.schedule.startHour < :end
                        AND l.schedule.endHour > :start
                  )
            """)
    List<Instructor> findAvailableInstructors(
            Especialidade category,
            LocalTime start,
            LocalTime end
    );
}
