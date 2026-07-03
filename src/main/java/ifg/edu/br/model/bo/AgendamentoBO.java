package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.AgendamentoDAO;
import ifg.edu.br.model.dto.AgendamentoRequestDTO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import ifg.edu.br.model.entity.AgendamentoEntity;
import ifg.edu.br.model.entity.ServicosEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class AgendamentoBO {

    @Inject
    AgendamentoDAO agendamentoDAO;

    @Transactional
    public String realizarAgendamento(AgendamentoRequestDTO agendamentoDTO) {
        System.out.println("Entrei no BO de AGENDAMENTO");
        AgendamentoRequestDTO erro = validar(agendamentoDTO);

        //verificando
        imprimirAgendamento(agendamentoDTO);

        if (erro == null) {
            return "Erro ao cadastrar: Validação incorreta!";
        }

        AgendamentoEntity agenda = new AgendamentoEntity();
        agenda.setServico(agendamentoDTO.getServico());
        agenda.setValor(agendamentoDTO.getValor());
        agenda.setDescricao(agendamentoDTO.getDescricao());
        agenda.setBarbeiroNome(agendamentoDTO.getBarbeiro());
        agenda.setDataAgendamento(agendamentoDTO.getData());
        agenda.setHoraAgendamento(agendamentoDTO.getHora());

        agendamentoDAO.salvar(agenda);

        return null;
    }

    private AgendamentoRequestDTO validar(AgendamentoRequestDTO agendamentoDTO) {
        if (campoVazio(agendamentoDTO.getServico()) || campoVazio(agendamentoDTO.getBarbeiro()) ) {
            System.out.println("Preencha todos os campos obrigatorios.");
            return null;
        }
        return agendamentoDTO;
    }//testando a validação SIMPLES desse DTO apenas para saber se está sendo passado.

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void imprimirAgendamento(AgendamentoRequestDTO agendamento) {
        System.out.println("Agendamento: " + agendamento.getServico());
        System.out.println("Valor: " + agendamento.getValor());
        System.out.println("Descricao: " + agendamento.getDescricao());
        System.out.println("Babeiro: " + agendamento.getBarbeiro());
        System.out.println("Data de Agendamento: " + agendamento.getData());
        System.out.println("Hora Agendamento: " + agendamento.getHora());
    }
}
