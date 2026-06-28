package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.ServicosDAO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import ifg.edu.br.model.entity.ServicosEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class ServicosBO {

    @Inject
    ServicosDAO servicosDAO;

    @Transactional
    public String cadastrarServicos(ServicoRequestDTO servicosDTO) {
        System.out.println("Entrei no BO de servico");
        ServicoRequestDTO erro = validar(servicosDTO);

        if (erro == null) {
            return "Erro ao cadastrar: Validação incorreta!";
        }

        ServicosEntity servico = new ServicosEntity();
        servico.setNome(servicosDTO.getNome());
        servico.setDescricao(servicosDTO.getDescricao());
        servico.setDuracaoMinutos(servicosDTO.getDuracaoMinutos());
        servico.setValor(servicosDTO.getValor());

        servicosDAO.salvar(servico);

        return null;
    }

    private ServicoRequestDTO validar(ServicoRequestDTO servicosDTO) {
        if (campoVazio(servicosDTO.getNome()) || campoVazio(servicosDTO.getDescricao()) ) {
            System.out.println("Preencha todos os campos obrigatorios.");
            return null;
        }
        return servicosDTO;
    }//testando a validação desse DTO

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }


}

