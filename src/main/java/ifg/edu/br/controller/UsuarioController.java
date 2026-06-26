package ifg.edu.br.controller;

import ifg.edu.br.model.bo.UsuarioBO;
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
    UsuarioBO usuarioBO;

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
    @Path("/cadastro/api")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response salvar(@BeanParam UsuarioDTO usuarioDTO) {
        String erro = usuarioBO.cadastrarUsuario(usuarioDTO);

        if (erro != null) {
            return Response.ok(Templates.Cadastro(erro, null)).build();
        }

        return Response.ok(Templates.Cadastro(null, "Usuario cadastrado com sucesso.")).build();
    }

}
