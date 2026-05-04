package br.com.senai.autoescola.n116.instructors.list;

import org.springframework.data.domain.Pageable;

public record ListInstructorsRequest(Pageable pageable) {
}
