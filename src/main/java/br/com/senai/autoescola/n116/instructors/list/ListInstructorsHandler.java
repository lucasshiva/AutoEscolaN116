package br.com.senai.autoescola.n116.instructors.list;

import br.com.senai.autoescola.n116.instructors.InstructorsRepository;
import org.springframework.stereotype.Component;

@Component
public class ListInstructorsHandler {
    private final InstructorsRepository repository;

    public ListInstructorsHandler(InstructorsRepository repository) {
        this.repository = repository;
    }

    public ListInstructorsResponse handle(ListInstructorsRequest req) {
        var page = repository.findAll(req.pageable());
        var data = page.map(student -> new ListInstructorDTO(
                student.getNome(),
                student.getEmail(),
                student.getCnh(),
                student.getEspecialidade())
        ).toList();

        return new ListInstructorsResponse(
                data,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
