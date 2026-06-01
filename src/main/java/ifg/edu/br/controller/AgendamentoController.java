package ifg.edu.br.controller;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/telaAgendamento")
public class AgendamentoController {

    @CheckedTemplate
    public static class Templates{
        public static native TemplateInstance TelaAgendamento();
        public static native TemplateInstance AgendamentoUsuario();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance Agendamento(){//é apenas um metodo
        //apenas a representação do Agendamento, o direcionamento da pagina.
        return Templates.TelaAgendamento();
    }

    @Path("/AgendamentoUsuario")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance AgendamentoUsuário(){//é apenas um metodo
        //apenas a representação do Agendamento, o direcionamento da pagina.
        return Templates.AgendamentoUsuario();
    }
}
