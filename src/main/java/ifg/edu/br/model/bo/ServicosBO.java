package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.ServicosDAO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import ifg.edu.br.model.entity.ServicosEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class ServicosBO {

    @Inject
    ServicosDAO servicosDAO;

    public List<ServicosEntity> listarTodos() {
        return servicosDAO.listarTodos();
    }

    @Transactional
    public String cadastrarServicos(ServicoRequestDTO servicosDTO) {
        System.out.println("Entrei no BO de servico");
        ServicoRequestDTO erro = validar(servicosDTO);

        if (erro == null) {
            return "Erro ao cadastrar: Validação incorreta!";
        }

        ServicosEntity servico = new ServicosEntity();
        servico.setNome(servicosDTO.nome());
        servico.setDescricao(servicosDTO.descricao());
        servico.setDuracaoMinutos(servicosDTO.duracaoMinutos());
        servico.setValor(servicosDTO.valor());

        servicosDAO.salvar(servico);

        return null;
    }

    private ServicoRequestDTO validar(ServicoRequestDTO servicosDTO) {
        if (campoVazio(servicosDTO.nome()) || campoVazio(servicosDTO.descricao()) ) {
            System.out.println("Preencha todos os campos obrigatorios.");
            return null;
        }
        return servicosDTO;
    }//testando a validação desse DTO

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }


}

