package ifg.edu.br.model.dto;

public record BarbeiroResponseDTO(
    Long id,
    String nome,
    String iniciais,
    String especialidade
) {}
