package ifg.edu.br.controller;

import ifg.edu.br.model.bo.PerfilBO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("perfil")
public class PerfilController {
    @Inject
    PerfilBO perfilBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance perfil();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance html() {
        return PerfilController.Templates.perfil();
    }

    @Path("save")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(String nome) {
        return perfilBO.salvar(nome);
    }

    @Path("list")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        return perfilBO.listarTodos();
    }
}