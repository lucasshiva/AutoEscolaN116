package br.com.senai.autoescola.n116.instructors;

public class DuplicateCnhException extends RuntimeException {
    public DuplicateCnhException(String cnh) {
        super("CNH " + cnh + " is already registered");
    }
}
