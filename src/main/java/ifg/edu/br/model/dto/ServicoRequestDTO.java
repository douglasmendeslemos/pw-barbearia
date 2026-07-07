package ifg.edu.br.model.dto;

public record ServicoRequestDTO(
        String nome,
        String descricao,
        Double valor,
        Integer duracaoMinutos
) {}