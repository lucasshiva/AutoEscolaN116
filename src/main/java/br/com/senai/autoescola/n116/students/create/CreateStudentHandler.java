package br.com.senai.autoescola.n116.students.create;

import br.com.senai.autoescola.n116.common.interfaces.EndpointHandler;
import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CreateStudentHandler implements EndpointHandler<CreateStudentCommand, CreateStudentResponse> {
    private final StudentsRepository studentsRepository;

    public CreateStudentHandler(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public CreateStudentResponse handle(CreateStudentCommand cmd) {
        Student entity = toEntity(cmd);
        Student student = studentsRepository.save(entity);
        return new CreateStudentResponse(student.getId(), student.getCreatedAt());
    }

    private Student toEntity(CreateStudentCommand cmd) {
        return new Student(
                null,
                cmd.nome(),
                cmd.email(),
                cmd.telefone(),
                cmd.endereco(),
                cmd.cpf(),
                Instant.now(),
                null,
                null
        );
    }
}
