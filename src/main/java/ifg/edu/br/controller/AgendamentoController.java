package ifg.edu.br.controller;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/agendamentos")
public class AgendamentoController {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance AgendamentoNew();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance listar() {
        return Templates.AgendamentoNew();
    }
}
