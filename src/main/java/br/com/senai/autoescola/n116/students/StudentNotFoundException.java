package br.com.senai.autoescola.n116.students;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(Long id) {
        super("Student not found with id " + id);
    }
}
