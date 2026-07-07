package ifg.edu.br.controller;

import ifg.edu.br.model.bo.AgendamentoBO;
import ifg.edu.br.model.dto.AgendamentoRequestDTO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/agendamentoUser")
public class AgendamentoUser {

    private final AgendamentoBO agendamentoBO;

    @Inject
    public AgendamentoUser(AgendamentoBO agendamentoBO) {
        this.agendamentoBO = agendamentoBO;
    }

    @CheckedTemplate
    @Authenticated
    public static class Templates {
        public static native TemplateInstance AgendamentoUser();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance AgendamentoUser() {
        return Templates.AgendamentoUser();
    }

    @POST
    @Path("/agendar/api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(AgendamentoRequestDTO agendamentoDTO) {
        System.out.println("NOVO AGENDAMENTO... CADASTRANDO...");
        String erro = agendamentoBO.realizarAgendamento(agendamentoDTO);

        System.out.println("verificou e criou o string erro caso seja necessario");
        if (erro != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        System.out.println("Agendamento - Realizado com sucesso");

        return Response.ok("Agendamento - Realizado com sucesso.")
                .build();
    }
}
