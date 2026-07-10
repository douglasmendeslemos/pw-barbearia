package ifg.edu.br.controller;

import ifg.edu.br.model.bo.AgendamentoBO;
import ifg.edu.br.model.bo.ServicosBO;
import ifg.edu.br.model.dao.UsuarioDAO;
import ifg.edu.br.model.dto.ServicoRequestDTO;
import ifg.edu.br.model.entity.AgendamentoEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.jwt.JsonWebToken;
import ifg.edu.br.model.dto.AgendamentoResponseDTO;

@Path("/agendamentoBarbeiro")
@RolesAllowed({"ADMINISTRADOR", "Barbeiro"})
public class AgendamentoBarberController {

    @Inject
    ServicosBO servicosBO;

    @Inject
    AgendamentoBO agendamentoBO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    JsonWebToken jwt;

    @GET
    @Path("/meus-agendamentos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response meusAgendamentos() {
        String email = jwt.getName();
        UsuarioEntity usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
        
        List<AgendamentoEntity> agendamentos;
        if (jwt.getGroups() != null && jwt.getGroups().contains("ADMINISTRADOR")) {
            agendamentos = agendamentoBO.listarTodosAgendamentos();
        } else {
            agendamentos = agendamentoBO.listarAgendamentosDoBarbeiro(usuario.getNome());
        }
        
        List<AgendamentoResponseDTO> dtos = agendamentos.stream()
                .map(e -> new AgendamentoResponseDTO(
                    e.getId(),
                    e.getServico(),
                    e.getValor(),
                    e.getDescricao(),
                    e.getBarbeiroNome(),
                    e.getDataAgendamento(),
                    e.getHoraAgendamento(),
                    e.getCliente() != null ? new AgendamentoResponseDTO.ClienteResponseDTO(
                        e.getCliente().getId(),
                        e.getCliente().getNome(),
                        e.getCliente().getEmail()
                    ) : null
                ))
                .toList();
                
        return Response.ok(dtos).build();
    }

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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        System.out.println("CADASTRO DE SERVIÇO - Realizado com sucesso");

        return Response.ok("Serviço cadastrado com sucesso.")
                .build();
    }
}
