package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.common.models.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;

    @Embedded
    private Address endereco;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "created_at", updatable = false, insertable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt = null;

    @Column(name = "deleted_at")
    private Instant deletedAt = null;
}
