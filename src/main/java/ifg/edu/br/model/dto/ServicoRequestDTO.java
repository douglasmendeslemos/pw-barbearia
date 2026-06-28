package ifg.edu.br.model.dto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor @AllArgsConstructor
@ApplicationScoped
public class ServicoRequestDTO{

    @Getter @Setter
    private String nome;
    @Getter @Setter
    private String descricao;
    @Getter @Setter
    private Double valor;
    @Getter @Setter
    private Integer duracaoMinutos;
}
