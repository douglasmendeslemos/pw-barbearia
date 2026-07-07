package ifg.edu.br.controller;

import ifg.edu.br.model.bo.ServicosBO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import ifg.edu.br.model.dto.UsuarioDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/agendamentoBarbeiro")
public class AgendamentoBarberController {

    @Inject
    ServicosBO servicosBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance agendamentoBarber();
        public static native TemplateInstance servicos(String mensagemErro, String mensagemSucesso);
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
        return Templates.servicos(null, null);
    }

    @POST
    @Path("/servicos/api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response salvar(ServicoRequestDTO servicoDTO) {
        System.out.println("NOVO SERVIÇO CADASTRANDO...");
        String erro = servicosBO.cadastrarServicos(servicoDTO);

        System.out.println("verificou e criou o string erro");
        if (erro != null) {
            return Response.ok(Templates
                    .servicos(erro, "Ocorreu um erro no servidor"))
                    .build();

        }
        System.out.println("CADASTRO DE SERVIÇO - Realizado com sucesso");

        return Response.ok("Serviço cadastrado com sucesso.")
                .build();
    }
}
