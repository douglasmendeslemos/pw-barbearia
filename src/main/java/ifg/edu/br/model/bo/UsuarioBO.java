package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.AuthResultadoDTO;
import ifg.edu.br.model.dto.LoginRequestDTO;
import ifg.edu.br.model.dto.UsuarioDTO;
import ifg.edu.br.model.entity.PerfilEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequestScoped
public class UsuarioBO {

    @Inject
    UsuarioDAO usuarioDAO;


    public AuthResultadoDTO realizarLogin(LoginRequestDTO loginRequestDTO) {

        UsuarioEntity usuario = usuarioDAO.buscarPorEmail(loginRequestDTO.email());
        if (usuario == null || !usuario.isAtivo()) {
            throw new WebApplicationException("login inválido", 401);
        }

        boolean senha = BcryptUtil.matches(loginRequestDTO.senha(), usuario.getSenha());

        if (!senha) {
            throw new WebApplicationException("login inválido", 401);
        }

        String token = Jwt.issuer("https://inventario.ifg.br")
                .upn(usuario.getEmail()) //email do usuário logado
                .groups(usuario.getPerfil().name()) // Perfil do usuário, grupo que ele faz parte
                .claim("id", usuario.getId()) // Recupera a Id do usuário com uma chamada rotulada
                .expiresIn(3600) // 1 hora em segundos
                .sign(); //Assina digitalmente e gera a string final

        return new AuthResultadoDTO(token, usuario.getNome(), usuario.getPerfil().name());

    }

    public String cadastrarUsuario(UsuarioDTO usuarioDTO) {
        String erro = validar(usuarioDTO);

        if (erro != null) {
            return erro;
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(usuarioDTO.getNome().trim());
        usuario.setEmail(usuarioDTO.getEmail().trim().toLowerCase());
        usuario.setSenhaHash(gerarHash(usuarioDTO.getSenha()));

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

    private String validar(UsuarioDTO usuarioDTO) {
        if (campoVazio(usuarioDTO.getNome()) || campoVazio(usuarioDTO.getEmail()) || campoVazio(usuarioDTO.getSenha())) {
            return "Preencha todos os campos obrigatorios.";
        }

        if (!usuarioDTO.senhasConferem()) {
            return "A senha e a confirmacao devem ser iguais.";
        }

        if (usuarioDAO.existeEmail(usuarioDTO.getEmail().trim())) {
            return "Ja existe um usuario cadastrado com este email.";
        }

        return null;
    }

    private boolean campoVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String gerarHash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultado = new StringBuilder();

            for (byte item : hash) {
                resultado.append(String.format("%02x", item));
            }

            return resultado.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Nao foi possivel gerar o hash da senha.", exception);
        }
    }

}
