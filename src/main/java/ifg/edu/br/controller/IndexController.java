package ifg.edu.br.controller;

import ifg.edu.br.model.bo.BarbeiroBO;
import ifg.edu.br.model.bo.ServicosBO;
import ifg.edu.br.model.entity.BarbeiroEntity;
import ifg.edu.br.model.entity.ServicosEntity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/")
public class IndexController {

    @Inject
    ServicosBO servicosBO;

    @Inject
    BarbeiroBO barbeiroBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {
        return Templates.index();
    }

    @GET
    @Path("/api/servicos")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarServicosPublicos() {
        List<ServicosEntity> servicos = servicosBO.listarTodos();
        return Response.ok(servicos).build();
    }

    @GET
    @Path("/api/barbeiros")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarBarbeirosPublicos() {
        List<BarbeiroEntity> barbeiros = barbeiroBO.listarTodos();
        return Response.ok(barbeiros).build();
    }
}
