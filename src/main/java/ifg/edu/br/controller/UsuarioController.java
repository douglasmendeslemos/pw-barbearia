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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(UsuarioDTO usuarioDTO) {
        System.out.println("Realizando o salvamento do novo cadastro");
        String erro = usuarioBO.cadastrarUsuario(usuarioDTO);

        if (erro != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        System.out.println("CADASTRO - Realizado com sucesso");
        return Response.ok("Usuário cadastrado com sucesso.")
                .build();
    }

}
