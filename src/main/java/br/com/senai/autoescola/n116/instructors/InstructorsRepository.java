package br.com.senai.autoescola.n116.instructors;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorsRepository extends JpaRepository<Instructor, Long> {
    boolean existsByCnh(String cnh);
}
