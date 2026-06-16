package ifg.edu.br.controller;

import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.UsuarioDTO;
import ifg.edu.br.model.entity.UsuarioEntity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Path("/usuarios")
public class UsuarioController {

    @Inject
    UsuarioDAO usuarioDAO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance Cadastro(String mensagemErro, String mensagemSucesso);
    }

    @GET
    @Path("/cadastro")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance cadastro() {
        return Templates.Cadastro(null, null);
    }

    @POST
    @Path("/cadastro")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response salvar(@BeanParam UsuarioDTO usuarioDTO) {
        String erro = cadastrarUsuario(usuarioDTO);

        if (erro != null) {
            return Response.ok(Templates.Cadastro(erro, null)).build();
        }

        return Response.ok(Templates.Cadastro(null, "Usuario cadastrado com sucesso.")).build();
    }

    @POST
    @Path("/cadastro/api")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response salvarViaJavaScript(@BeanParam UsuarioDTO usuarioDTO) {
        String erro = cadastrarUsuario(usuarioDTO);

        if (erro != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(erro).build();
        }

        return Response.ok("Usuario cadastrado com sucesso.").build();
    }

    private String cadastrarUsuario(UsuarioDTO usuarioDTO) {
        String erro = validar(usuarioDTO);

        if (erro != null) {
            return erro;
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(usuarioDTO.getNome().trim());
        usuario.setEmail(usuarioDTO.getEmail().trim().toLowerCase());
        usuario.setSenhaHash(gerarHash(usuarioDTO.getSenha()));

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
