package br.com.senai.autoescola.n116.students.create;

import br.com.senai.autoescola.n116.common.interfaces.EndpointHandler;
import br.com.senai.autoescola.n116.students.Student;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateStudentHandler implements EndpointHandler<CreateStudentRequest, CreateStudentResponse> {
    private final StudentsRepository studentsRepository;

    public CreateStudentHandler(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public CreateStudentResponse handle(CreateStudentRequest cmd) {
        Student entity = toEntity(cmd); // Manually using `Instant.now()` inside `toEntity(cmd)`.
        Student student = studentsRepository.save(entity);  // DB should set `createAt`.
        return new CreateStudentResponse(student.getId(), student.getCreatedAt());
    }

    private Student toEntity(CreateStudentRequest cmd) {
        return new Student(
                null,
                cmd.nome(),
                cmd.email(),
                cmd.telefone(),
                cmd.endereco(),
                cmd.cpf(),
                null,
                null,
                null
        );
    }
}
