package ifg.edu.br.model.dto;

public record AgendamentoResponseDTO(
    Long id,
    String servico,
    Double valor,
    String descricao,
    String barbeiroNome,
    String dataAgendamento,
    String horaAgendamento,
    ClienteResponseDTO cliente
) {
    public record ClienteResponseDTO(
        Long id,
        String nome,
        String email
    ) {}
}
