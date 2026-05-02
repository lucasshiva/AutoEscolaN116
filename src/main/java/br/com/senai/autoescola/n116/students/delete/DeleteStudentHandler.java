package br.com.senai.autoescola.n116.students.delete;

import br.com.senai.autoescola.n116.common.interfaces.IHandler;
import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteStudentHandler implements IHandler<DeleteStudentRequest, Void> {
    private final StudentsRepository studentsRepository;

    public DeleteStudentHandler(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public Void handle(DeleteStudentRequest request) {
        var student = studentsRepository
                .findById(request.id())
                .orElseThrow(() -> new StudentNotFoundException(request.id()));

        studentsRepository.delete(student);
        return null;
    }
}
