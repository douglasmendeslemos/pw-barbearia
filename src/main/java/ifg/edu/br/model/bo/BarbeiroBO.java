package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.BarbeiroDAO;
import ifg.edu.br.model.dao.PerfilDAO;
import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.BarbeiroDTO;
import ifg.edu.br.model.entity.BarbeiroEntity;
import ifg.edu.br.model.entity.PerfilEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class BarbeiroBO {

    @Inject
    BarbeiroDAO barbeiroDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    PerfilDAO perfilDAO;

    @Transactional
    public String cadastrarBarbeiro(BarbeiroDTO dto) {
        if (dto.nome() == null || dto.nome().trim().isEmpty() ||
            dto.email() == null || dto.email().trim().isEmpty() ||
            dto.senha() == null || dto.senha().trim().isEmpty()) {
            return "Preencha todos os campos obrigatorios.";
        }

        if (usuarioDAO.existeEmail(dto.email().trim().toLowerCase())) {
            return "Ja existe um usuario cadastrado com este email.";
        }

        // Buscar perfil de barbeiro no banco
        PerfilEntity perfilBarbeiro = perfilDAO.buscarPorNome("Barbeiro");
        if (perfilBarbeiro == null) {
            // Caso o perfil "Barbeiro" nao exista, cria dinamicamente
            perfilBarbeiro = new PerfilEntity();
            perfilBarbeiro.setNomePerfil("Barbeiro");
            perfilDAO.save(perfilBarbeiro);
        }

        // Cria o usuario para o login do barbeiro
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(dto.email().trim().toLowerCase());
        usuario.setSenhaHash(BcryptUtil.bcryptHash(dto.senha()));
        usuario.setPerfil(perfilBarbeiro);

        // Cria o barbeiro com as informacoes adicionais
        BarbeiroEntity barbeiro = new BarbeiroEntity();
        barbeiro.setNome(dto.nome().trim());
        barbeiro.setIniciais(dto.iniciais() != null ? dto.iniciais().trim().toUpperCase() : "");
        barbeiro.setEspecialidade(dto.especialidade() != null ? dto.especialidade().trim() : "");
        barbeiro.setUsuario(usuario);

        barbeiroDAO.salvar(barbeiro);
        return null;
    }

    public List<BarbeiroEntity> listarTodos() {
        return barbeiroDAO.listarTodos();
    }
}
