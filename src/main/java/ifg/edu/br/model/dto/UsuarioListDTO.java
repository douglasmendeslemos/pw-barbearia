package ifg.edu.br.model.dto;

public record UsuarioListDTO (
    Long id,
    String nome,
    String email,
    Long perfilId,
    String perfilNome
){}