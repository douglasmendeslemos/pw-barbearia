package ifg.edu.br.model.dto;

public record UsuarioDTO(
        String nome,
        String email,
        String senha,
        String confirmacaoSenha
){}
