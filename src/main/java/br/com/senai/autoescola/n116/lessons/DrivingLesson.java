package br.com.senai.autoescola.n116.lessons;

import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.students.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "driving_lessons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
public class DrivingLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_student", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_instructor", nullable = false)
    private Instructor instructor;

    @Embedded
    private LessonSchedule schedule;

    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;
}
