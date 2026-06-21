package ifg.edu.br.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbperfis", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nomePerfil", name = "tbperfil_nome_uk")
})
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String nomePerfil;


}
