package br.com.senai.autoescola.n116.instructors.list;

import java.util.List;

public record ListInstructorsResponse(
        List<ListInstructorDTO> data,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}