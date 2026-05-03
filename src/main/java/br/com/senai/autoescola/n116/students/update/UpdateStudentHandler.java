package br.com.senai.autoescola.n116.students.update;

import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateStudentHandler {
    private final StudentsRepository repository;

    public UpdateStudentHandler(StudentsRepository repository) {
        this.repository = repository;
    }

    public UpdateStudentResponse handle(Long id, UpdateStudentRequest request) {
        Student student = repository
                .findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.update(request);
        var saved = repository.save(student);

        return new UpdateStudentResponse(
                saved.getId(),
                saved.getNome(),
                saved.getEmail(),
                saved.getTelefone(),
                saved.getCpf(),
                saved.getEndereco(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
