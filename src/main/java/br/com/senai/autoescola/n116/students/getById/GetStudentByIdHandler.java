package br.com.senai.autoescola.n116.students.getById;

import br.com.senai.autoescola.n116.students.StudentNotFoundException;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class GetStudentByIdHandler {
    private final StudentsRepository repository;

    public GetStudentByIdHandler(StudentsRepository repository) {
        this.repository = repository;
    }

    public GetStudentByIdResponse handle(GetStudentByIdRequest getStudentByIdRequest) {
        var id = getStudentByIdRequest.id();
        return repository
                .findById(id)
                .map(s -> new GetStudentByIdResponse(
                        s.getId(),
                        s.getNome(),
                        s.getEmail(),
                        s.getTelefone(),
                        s.getEndereco(),
                        s.getCpf(),
                        s.getCreatedAt(),
                        s.getUpdatedAt()
                ))
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
}
