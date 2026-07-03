package ifg.edu.br.model.dto;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class AgendamentoRequestDTO {
    @Getter @Setter
    private String servico;
    @Getter @Setter
    private Double valor;
    @Getter @Setter
    private String descricao;
    @Getter @Setter
    private String barbeiroNome;
    @Getter @Setter
    private String dataAgendamento;
    @Getter @Setter
    private String horaAgendamento;
}