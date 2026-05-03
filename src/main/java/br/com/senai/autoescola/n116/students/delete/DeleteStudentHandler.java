package br.com.senai.autoescola.n116.students.delete;

import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteStudentHandler {
    private final StudentsRepository studentsRepository;

    public DeleteStudentHandler(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public void handle(DeleteStudentRequest request) {
        var student = studentsRepository
                .findById(request.id())
                .orElseThrow(() -> new StudentNotFoundException(request.id()));

        studentsRepository.delete(student);
    }
}
