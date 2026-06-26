package ifg.edu.br.controller;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/agendamentoUser")
public class AgendamentoUser {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance AgendamentoUser();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance AgendamentoUser() {
        return Templates.AgendamentoUser();
    }

}
