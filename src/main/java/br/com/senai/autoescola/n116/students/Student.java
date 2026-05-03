package br.com.senai.autoescola.n116.students;

import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.students.update.UpdateStudentRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Email
    private String email;

    @Pattern(regexp = "\\d{11}", message = "must be exactly 11 digits")
    private String telefone;

    @Embedded
    private Address endereco;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "\\d{11}", message = "must be exactly 11 digits")
    private String cpf;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt = null;

    public void update(UpdateStudentRequest request) {
        this.nome = request.nome();
        this.telefone = request.telefone();
        this.endereco = request.endereco();
    }
}
