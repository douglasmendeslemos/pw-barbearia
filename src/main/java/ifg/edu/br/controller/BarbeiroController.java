package ifg.edu.br.controller;

import ifg.edu.br.model.bo.BarbeiroBO;
import ifg.edu.br.model.dto.BarbeiroDTO;
import ifg.edu.br.model.entity.BarbeiroEntity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/barbeiros")
@RolesAllowed("ADMINISTRADOR")
public class BarbeiroController {

    @Inject
    BarbeiroBO barbeiroBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance barbeiros();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance html() {
        return BarbeiroController.Templates.barbeiros();
    }

    @POST
    @Path("/api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(BarbeiroDTO barbeiroDTO) {
        String erro = barbeiroBO.cadastrarBarbeiro(barbeiroDTO);
        if (erro != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensagem\": \"" + erro + "\"}")
                    .build();
        }
        return Response.ok("{\"mensagem\": \"Barbeiro cadastrado com sucesso.\"}").build();
    }

    @GET
    @Path("/api")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listar() {
        List<BarbeiroEntity> barbeiros = barbeiroBO.listarTodos();
        return Response.ok(barbeiros).build();
    }
}
