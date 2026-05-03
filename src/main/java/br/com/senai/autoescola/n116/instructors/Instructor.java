package br.com.senai.autoescola.n116.instructors;

import br.com.senai.autoescola.n116.common.annotations.CNH;
import br.com.senai.autoescola.n116.common.annotations.Telefone;
import br.com.senai.autoescola.n116.common.models.Address;
import br.com.senai.autoescola.n116.common.models.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "instructors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@SoftDelete(columnName = "deleted_at", strategy = SoftDeleteType.TIMESTAMP)
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Email
    private String email;

    @Telefone
    private String telefone;

    @Column(unique = true, length = 10)
    @CNH
    private String cnh;

    @Enumerated(EnumType.STRING)
    private Category especialidade;

    @Embedded
    private Address endereco;

    @Column(name = "created_at", updatable = false, insertable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt = null;
}
