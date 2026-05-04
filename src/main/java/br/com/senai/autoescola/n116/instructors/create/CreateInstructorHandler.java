package br.com.senai.autoescola.n116.instructors.create;

import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateInstructorHandler {
    private final InstructorsRepository repository;

    public CreateInstructorHandler(InstructorsRepository repository) {
        this.repository = repository;
    }

    public CreateInstructorResponse handle(CreateInstructorRequest req) {
        var instructor = toEntity(req);
        var saved = repository.save(instructor);
        return new CreateInstructorResponse(saved.getId(), saved.getNome(), saved.getCreatedAt());
    }

    private Instructor toEntity(CreateInstructorRequest req) {
        return new Instructor(
                null,
                req.nome(),
                req.email(),
                req.telefone(),
                req.cnh(),
                req.especialidade(),
                req.endereco(),
                null,
                null
        );
    }
}
