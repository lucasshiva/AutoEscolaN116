package br.com.senai.autoescola.n116.students;

public class DuplicateCpfException extends RuntimeException {
    public DuplicateCpfException(String cpf) {
        super("CPF " + cpf + " is already registered");
    }
}
