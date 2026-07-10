package ifg.edu.br.model.dto;

public record ServicoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Double valor,
    Integer duracaoMinutos
) {}
