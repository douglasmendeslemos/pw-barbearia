package ifg.edu.br.controller;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/agendamentoBarbeiro")
public class AgendamentoBarberController {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance agendamentoBarber();
        public static native TemplateInstance servicos();
    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance agendamentoBarber() {
        return Templates.agendamentoBarber();
    }

    @Path("/servicos")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance servicos() {
        return Templates.servicos();
    }

}
