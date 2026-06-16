package ifg.edu.br.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioListDTO {

    private Integer id;
    private String nome;
    private String perfil;

}