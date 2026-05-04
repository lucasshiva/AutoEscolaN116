package br.com.senai.autoescola.n116.instructors.delete;

import br.com.senai.autoescola.n116.instructors.InstructorNotFoundException;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteInstructorHandler {
    private final InstructorsRepository repository;

    public DeleteInstructorHandler(InstructorsRepository repository) {
        this.repository = repository;
    }

    public void handle(Long id) {
        var instructor = repository
                .findById(id)
                .orElseThrow(() -> new InstructorNotFoundException(id));

        repository.delete(instructor);
    }
}
