package br.com.senai.autoescola.n116.students.list;

import org.springframework.data.domain.Pageable;

public record ListStudentsRequest(Pageable pageable) {
}
