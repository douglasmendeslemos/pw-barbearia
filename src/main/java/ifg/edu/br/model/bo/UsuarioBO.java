package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.AuthResultadoDTO;
import ifg.edu.br.model.dto.LoginRequestDTO;
import ifg.edu.br.model.dto.UsuarioDTO;
import ifg.edu.br.model.entity.PerfilEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import io.quarkus.elytron.security.common.BcryptUtil;
import org.jetbrains.annotations.NotNull;

@RequestScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;


    public AuthResultadoDTO realizarLogin(LoginRequestDTO loginRequestDTO) {

        System.out.println("Email recebido: " + loginRequestDTO.email());

        UsuarioEntity usuario = usuarioDAO.buscarPorEmail(loginRequestDTO.email());
        if (usuario == null) {
            System.out.println("Não encontrado ERRO AQUI ");
            throw new WebApplicationException("login inválido", 401);
        }
        System.out.println("Usuário encontrado: " + usuario);

        boolean senha = BcryptUtil.matches(loginRequestDTO.senha(), usuario.getSenhaHash());
        System.out.println("Senha válida: " + senha);

        if (!senha) {
            System.out.println("ERRO de SENHA INVALIDA");
            throw new WebApplicationException("login inválido", 401);
        }

        String token = Jwt.issuer("https://barbearia.ifg.br")
                .upn(usuario.getEmail()) //email do usuário logado
                .groups(usuario.getPerfil().getNomePerfil()) // Perfil do usuário, grupo que ele faz parte
                .claim("id", usuario.getId()) // Recupera a Id do usuário com uma chamada rotulada
                .expiresIn(3600) // 1 hora em segundos
                .sign(); //Assina digitalmente e gera a string final
        System.out.println(usuario.getPerfil().getNomePerfil());
        System.out.println("Token gerado.");
        System.out.println(token);
        return new AuthResultadoDTO(token, usuario.getNome(), usuario.getPerfil().getNomePerfil());

    }

    public String cadastrarUsuario(UsuarioDTO usuarioDTO) {
        String erro = validar(usuarioDTO);

        if (erro != null) {
            return erro;
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(usuarioDTO.nome().trim());
        usuario.setEmail(usuarioDTO.email().trim().toLowerCase());
        usuario.setSenhaHash(BcryptUtil.bcryptHash(usuarioDTO.senha()));//Alterando para Bcrypt


        // ==========================================
        // CORREÇÃO: Definindo o perfil padrão (ID 5)
        // ==========================================
        PerfilEntity perfilCliente = new PerfilEntity();
        perfilCliente.setId(5L);

        usuario.setPerfil(perfilCliente);
        // ==========================================

        usuarioDAO.salvar(usuario);

        return null;
    }

    @Nullable
    private String validar(@NotNull UsuarioDTO usuarioDTO) {
        if (campoVazio(usuarioDTO.nome()) || campoVazio(usuarioDTO.email()) || campoVazio(usuarioDTO.senha())) {
            return "Preencha todos os campos obrigatorios.";
        }

        if (!usuarioDTO.senha().equals(usuarioDTO.confirmacaoSenha())) {
            return "A senha e a confirmacao devem ser iguais.";
        }

        if (usuarioDAO.existeEmail(usuarioDTO.email().trim().toLowerCase())) {
            return "Ja existe um usuario cadastrado com este email.";
        }

        return null;
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }



    public java.util.List<UsuarioEntity> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    @Transactional
    public void alterarPerfil(Long usuarioId, Long perfilId) {
        UsuarioEntity usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new jakarta.ws.rs.WebApplicationException("Usuario nao encontrado", 404);
        }
        PerfilEntity perfil = new PerfilEntity();
        perfil.setId(perfilId);
        usuario.setPerfil(perfil);
        usuarioDAO.atualizar(usuario);
    }

}
