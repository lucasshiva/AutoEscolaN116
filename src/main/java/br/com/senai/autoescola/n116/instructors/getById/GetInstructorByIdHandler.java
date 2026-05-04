package br.com.senai.autoescola.n116.instructors.getById;

import br.com.senai.autoescola.n116.instructors.InstructorNotFoundException;
import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import org.springframework.stereotype.Component;

@Component
public class GetInstructorByIdHandler {
    private final InstructorsRepository repository;

    public GetInstructorByIdHandler(InstructorsRepository repository) {
        this.repository = repository;
    }

    public GetInstructorByIdResponse handle(GetInstructorByIdRequest req) {
        return repository
                .findById(req.id())
                .map(entity -> new GetInstructorByIdResponse(
                        entity.getId(),
                        entity.getNome(),
                        entity.getEmail(),
                        entity.getTelefone(),
                        entity.getCnh(),
                        entity.getEspecialidade(),
                        entity.getEndereco(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                ))
                .orElseThrow(() -> new InstructorNotFoundException(req.id()));
    }
}
