package br.com.senai.autoescola.n116.instructors.update;

import br.com.senai.autoescola.n116.instructors.Instructor;
import br.com.senai.autoescola.n116.instructors.InstructorNotFoundException;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateInstructorHandler {
    private final InstructorsRepository repository;

    public UpdateInstructorHandler(InstructorsRepository repository) {
        this.repository = repository;
    }

    public UpdateInstructorResponse handle(Long id, UpdateInstructorRequest req) {
        Instructor instructor = repository
                .findById(id)
                .orElseThrow(() -> new InstructorNotFoundException(id));

        instructor.update(req);
        var saved = repository.save(instructor);

        return new UpdateInstructorResponse(
                saved.getId(),
                saved.getNome(),
                saved.getEmail(),
                saved.getTelefone(),
                saved.getCnh(),
                saved.getEndereco(),
                saved.getEspecialidade(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
