package br.com.senai.autoescola.n116.students.list;

import java.util.List;

public record ListStudentsResponse(
        List<ListStudentDTO> data,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
