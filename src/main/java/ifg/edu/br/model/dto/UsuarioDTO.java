package ifg.edu.br.model.dto;

import jakarta.ws.rs.FormParam;
import lombok.*;

@NoArgsConstructor // construtor padrão vazio.
@AllArgsConstructor //feito para ter todos tipos de construtores
@Data //uma forma mais simples de
public class UsuarioDTO {

    @FormParam("nome")
    private String nome;

    @FormParam("email")
    private String email;

    @FormParam("senha")
    private String senha;

    @FormParam("confirmacaoSenha")
    private String confirmacaoSenha;

    public boolean senhasConferem() {
        return senha != null && senha.equals(confirmacaoSenha);
    }
}
