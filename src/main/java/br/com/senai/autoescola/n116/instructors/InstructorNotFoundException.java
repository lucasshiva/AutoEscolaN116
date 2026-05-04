package br.com.senai.autoescola.n116.instructors;

public class InstructorNotFoundException extends RuntimeException {
    public InstructorNotFoundException(Long id) {
        super("Instructor with ID " + id + " not found");
    }
}
