package ifg.edu.br.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbusuarios")
@Data
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false, name = "senha_hash", length = 64)
    private String senhaHash;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    @Getter @Setter
    private PerfilEntity perfil;
}
