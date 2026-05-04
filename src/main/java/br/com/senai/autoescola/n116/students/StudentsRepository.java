package br.com.senai.autoescola.n116.students;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentsRepository extends JpaRepository<Student, Long> {
    boolean existsByCpf(String cpf);
}
