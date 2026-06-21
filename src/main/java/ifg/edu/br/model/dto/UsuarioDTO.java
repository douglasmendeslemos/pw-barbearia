package ifg.edu.br.model.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.ws.rs.FormParam;
import lombok.*;

@NoArgsConstructor // construtor padrão vazio.
@AllArgsConstructor //feito para ter todos tipos de construtores
@Data //uma forma mais simples usando lombok
public class UsuarioDTO {

    @NotNull
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
