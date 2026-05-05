CREATE TABLE driving_lessons
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    id_student BIGINT NOT NULL,
    id_instructor BIGINT NOT NULL,

    schedule_date DATE NOT NULL,
    schedule_start TIME NOT NULL,
    schedule_end TIME NOT NULL,

    -- This is how we track cancelled lessons.
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,

    deleted_at TIMESTAMP NULL,
    cancellation_reason VARCHAR(255) NULL,

    PRIMARY KEY (id),

    -- Foreign keys
    CONSTRAINT fk_student FOREIGN KEY (id_student) REFERENCES students(id),
    CONSTRAINT fk_instructor FOREIGN KEY (id_instructor) REFERENCES instructors(id),

    -- Unique constraints to prevent double-booking
    CONSTRAINT uq_student_schedule UNIQUE (id_student, schedule_date, schedule_start),
    CONSTRAINT uq_instructor_schedule UNIQUE (id_instructor, schedule_date, schedule_start)
);