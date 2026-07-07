package ifg.edu.br.model.dto;

public record AgendamentoRequestDTO(String servico,
        Double valor,
        String descricao,
        String barbeiro,
        String data,
        String hora
){}