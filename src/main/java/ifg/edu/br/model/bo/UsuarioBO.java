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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jetbrains.annotations.NotNull;

@RequestScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;


    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[cC][oO][mM]$";
    private static final String SENHA_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$";

    private boolean validarEmail(String email) {
        if (email == null) return false;
        return email.trim().toLowerCase().matches(EMAIL_REGEX);
    }

    private boolean validarSenha(String senha) {
        if (senha == null) return false;
        return senha.matches(SENHA_REGEX);
    }

    public AuthResultadoDTO realizarLogin(LoginRequestDTO loginRequestDTO) {
        System.out.println("Email recebido: " + loginRequestDTO.email());

        if (campoVazio(loginRequestDTO.email()) || campoVazio(loginRequestDTO.senha())) {
            throw new WebApplicationException(
                Response.status(jakarta.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity("Preencha todos os campos.")
                    .type(jakarta.ws.rs.core.MediaType.TEXT_PLAIN)
                    .build()
            );
        }

        if (!validarEmail(loginRequestDTO.email())) {
            throw new WebApplicationException(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity("Formato de e-mail inválido. Deve conter '@' e terminar com '.com'.")
                    .type(jakarta.ws.rs.core.MediaType.TEXT_PLAIN)
                    .build()
            );
        }

        if (!validarSenha(loginRequestDTO.senha())) {
            throw new WebApplicationException(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity("A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.")
                    .type(jakarta.ws.rs.core.MediaType.TEXT_PLAIN)
                    .build()
            );
        }

        UsuarioEntity usuario = usuarioDAO.buscarPorEmail(loginRequestDTO.email());
        if (usuario == null) {
            System.out.println("Não encontrado ERRO AQUI ");
            throw new WebApplicationException(
                Response.status(jakarta.ws.rs.core.Response.Status.UNAUTHORIZED)
                    .entity("login inválido")
                    .type(MediaType.TEXT_PLAIN)
                    .build()
            );
        }
        System.out.println("Usuário encontrado: " + usuario);

        boolean senha = BcryptUtil.matches(loginRequestDTO.senha(), usuario.getSenhaHash());
        System.out.println("Senha válida: " + senha);

        if (!senha) {
            System.out.println("ERRO de SENHA INVALIDA");
            throw new WebApplicationException(
                Response.status(jakarta.ws.rs.core.Response.Status.UNAUTHORIZED)
                    .entity("login inválido")
                    .type(MediaType.TEXT_PLAIN)
                    .build()
            );
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

        if (!validarEmail(usuarioDTO.email())) {
            return "Formato de e-mail inválido. Deve conter '@' e terminar com '.com'.";
        }

        if (!validarSenha(usuarioDTO.senha())) {
            return "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.";
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
