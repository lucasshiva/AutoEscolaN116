package br.com.senai.autoescola.n116.students.list;

import br.com.senai.autoescola.n116.common.interfaces.IHandler;
import br.com.senai.autoescola.n116.students.StudentsRepository;
import org.springframework.stereotype.Component;

@Component
public class ListStudentsHandler implements IHandler<ListStudentsRequest, ListStudentsResponse> {
    private final StudentsRepository studentsRepository;

    public ListStudentsHandler(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @Override
    public ListStudentsResponse handle(ListStudentsRequest request) {
        var page = studentsRepository.findAll(request.pageable());
        var data = page.map(student -> new ListStudentDTO(
                student.getNome(),
                student.getEmail(),
                student.getCpf())
        ).toList();

        return new ListStudentsResponse(
                data,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}