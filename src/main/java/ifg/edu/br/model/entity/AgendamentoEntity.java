package ifg.edu.br.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name= "agendamento")
@Data
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String servico;
    private Double valor;
    private String descricao;
    private String barbeiroNome;
    private String dataAgendamento;
    private String horaAgendamento;

}
