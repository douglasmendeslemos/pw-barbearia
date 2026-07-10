package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.AgendamentoDAO;
import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.AgendamentoRequestDTO;
import ifg.edu.br.model.entity.AgendamentoEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequestScoped
public class AgendamentoBO {

    @Inject
    AgendamentoDAO agendamentoDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Transactional
    public void limparAgendamentosAntigos() {
        List<AgendamentoEntity> todos = agendamentoDAO.buscarTodos();
        LocalDateTime agora = LocalDateTime.now();
        for (AgendamentoEntity agenda : todos) {
            try {
                LocalDate data = LocalDate.parse(agenda.getDataAgendamento());
                LocalTime hora = LocalTime.parse(agenda.getHoraAgendamento());
                LocalDateTime dataHoraAgendamento = LocalDateTime.of(data, hora);
                if (dataHoraAgendamento.isBefore(agora)) {
                    agendamentoDAO.deletar(agenda.getId());
                }
            } catch (Exception e) {
                System.err.println("Erro ao verificar/limpar agendamento antigo com ID " + agenda.getId() + ": " + e.getMessage());
            }
        }
    }

    @Transactional
    public String realizarAgendamento(AgendamentoRequestDTO agendamentoDTO, String clienteEmail) {
        System.out.println("Entrei no BO de AGENDAMENTO");
        
        // Limpa agendamentos passados antes de processar
        limparAgendamentosAntigos();

        AgendamentoRequestDTO erro = validar(agendamentoDTO);
        if (erro == null) {
            return "Erro ao cadastrar: Validação incorreta!";
        }

        // 1. Impede agendamentos no passado
        try {
            LocalDate data = LocalDate.parse(agendamentoDTO.data());
            LocalTime hora = LocalTime.parse(agendamentoDTO.hora());
            LocalDateTime dataHoraAgendamento = LocalDateTime.of(data, hora);
            if (dataHoraAgendamento.isBefore(LocalDateTime.now())) {
                return "Erro ao agendar: Não é possível realizar agendamentos para datas ou horários no passado.";
            }
        } catch (Exception e) {
            return "Erro ao agendar: Formato de data ou hora inválido.";
        }

        // 2. Gerenciador de conflito de horário para o mesmo barbeiro
        List<AgendamentoEntity> conflitos = agendamentoDAO.buscarPorBarbeiroDataHora(
            agendamentoDTO.barbeiro(), 
            agendamentoDTO.data(), 
            agendamentoDTO.hora()
        );
        if (!conflitos.isEmpty()) {
            return "Erro ao agendar: O barbeiro " + agendamentoDTO.barbeiro() + " já possui um agendamento neste horário.";
        }

        UsuarioEntity cliente = usuarioDAO.buscarPorEmail(clienteEmail);
        if (cliente == null) {
            return "Erro ao cadastrar: Cliente nao localizado.";
        }

        AgendamentoEntity agenda = new AgendamentoEntity();
        agenda.setServico(agendamentoDTO.servico());
        agenda.setValor(agendamentoDTO.valor());
        agenda.setDescricao(agendamentoDTO.descricao());
        agenda.setBarbeiroNome(agendamentoDTO.barbeiro());
        agenda.setDataAgendamento(agendamentoDTO.data());
        agenda.setHoraAgendamento(agendamentoDTO.hora());
        agenda.setCliente(cliente);

        agendamentoDAO.salvar(agenda);

        return null;
    }

    private AgendamentoRequestDTO validar(AgendamentoRequestDTO agendamentoDTO) {
        if (campoVazio(agendamentoDTO.servico()) || campoVazio(agendamentoDTO.barbeiro()) ) {
            System.out.println("Preencha todos os campos obrigatorios.");
            return null;
        }
        return agendamentoDTO;
    }//testando a validação SIMPLES desse DTO apenas para saber se está sendo passado.

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void imprimirAgendamento(AgendamentoRequestDTO agendamento) {
        System.out.println("Agendamento: " + agendamento.servico());
        System.out.println("Valor: " + agendamento.valor());
        System.out.println("Descricao: " + agendamento.descricao());
        System.out.println("Babeiro: " + agendamento.barbeiro());
        System.out.println("Data de Agendamento: " + agendamento.data());
        System.out.println("Hora Agendamento: " + agendamento.hora());
    }

    public java.util.List<AgendamentoEntity> listarAgendamentosDoBarbeiro(String barbeiroNome) {
        limparAgendamentosAntigos();
        return agendamentoDAO.buscarPorBarbeiro(barbeiroNome);
    }

    public java.util.List<AgendamentoEntity> listarAgendamentosDoCliente(String clienteEmail) {
        limparAgendamentosAntigos();
        return agendamentoDAO.buscarPorClienteEmail(clienteEmail);
    }

    public java.util.List<AgendamentoEntity> listarTodosAgendamentos() {
        limparAgendamentosAntigos();
        return agendamentoDAO.buscarTodos();
    }

    @Transactional
    public String atualizarAgendamento(Long id, AgendamentoRequestDTO agendamentoDTO, String clienteEmail) {
        limparAgendamentosAntigos();
        
        AgendamentoRequestDTO erro = validar(agendamentoDTO);
        if (erro == null) {
            return "Erro ao atualizar: Validação incorreta!";
        }

        // 1. Impede agendamento no passado
        try {
            java.time.LocalDate data = java.time.LocalDate.parse(agendamentoDTO.data());
            java.time.LocalTime hora = java.time.LocalTime.parse(agendamentoDTO.hora());
            java.time.LocalDateTime dataHoraAgendamento = java.time.LocalDateTime.of(data, hora);
            if (dataHoraAgendamento.isBefore(java.time.LocalDateTime.now())) {
                return "Erro ao atualizar: Não é possível realizar agendamentos para datas ou horários no passado.";
            }
        } catch (Exception e) {
            return "Erro ao atualizar: Formato de data ou hora inválido.";
        }

        // 2. Gerenciador de conflito de horário para o mesmo barbeiro
        java.util.List<AgendamentoEntity> conflitos = agendamentoDAO.buscarPorBarbeiroDataHora(
            agendamentoDTO.barbeiro(), 
            agendamentoDTO.data(), 
            agendamentoDTO.hora()
        );
        for (AgendamentoEntity c : conflitos) {
            if (!c.getId().equals(id)) {
                return "Erro ao atualizar: O barbeiro " + agendamentoDTO.barbeiro() + " já possui um agendamento neste horário.";
            }
        }

        AgendamentoEntity agenda = agendamentoDAO.buscarPorId(id);
        if (agenda == null) {
            return "Erro ao atualizar: Agendamento não encontrado.";
        }

        // Garante que o cliente logado é o dono do agendamento
        if (agenda.getCliente() == null || !agenda.getCliente().getEmail().equalsIgnoreCase(clienteEmail)) {
            return "Erro ao atualizar: Você não tem permissão para alterar este agendamento.";
        }

        agenda.setServico(agendamentoDTO.servico());
        agenda.setValor(agendamentoDTO.valor());
        agenda.setDescricao(agendamentoDTO.descricao());
        agenda.setBarbeiroNome(agendamentoDTO.barbeiro());
        agenda.setDataAgendamento(agendamentoDTO.data());
        agenda.setHoraAgendamento(agendamentoDTO.hora());

        agendamentoDAO.atualizar(agenda);
        return null;
    }

    @Transactional
    public String deletarAgendamento(Long id, String clienteEmail) {
        AgendamentoEntity agenda = agendamentoDAO.buscarPorId(id);
        if (agenda == null) {
            return "Erro ao deletar: Agendamento não encontrado.";
        }

        // Garante que o cliente logado é o dono do agendamento
        if (agenda.getCliente() == null || !agenda.getCliente().getEmail().equalsIgnoreCase(clienteEmail)) {
            return "Erro ao deletar: Você não tem permissão para remover este agendamento.";
        }

        agendamentoDAO.deletar(id);
        return null;
    }
}
