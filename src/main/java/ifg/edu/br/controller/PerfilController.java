package ifg.edu.br.controller;

import ifg.edu.br.model.bo.PerfilBO;
import ifg.edu.br.model.bo.UsuarioBO;
import ifg.edu.br.model.dto.UsuarioListDTO;
import ifg.edu.br.model.entity.UsuarioEntity;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("perfil")
@RolesAllowed("ADMINISTRADOR")
public class PerfilController {
    @Inject
    PerfilBO perfilBO;

    @Inject
    UsuarioBO usuarioBO;

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

    @Path("list-perfis")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodosPerfis() {
        return perfilBO.listarTodosPerfis();
    }

    @Path("usuarios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuarios() {
        List<UsuarioEntity> usuarios = usuarioBO.listarTodos();
        List<UsuarioListDTO> dtos = usuarios.stream()
                .map(u -> new UsuarioListDTO(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getPerfil() != null ? u.getPerfil().getId() : null,
                        u.getPerfil() != null ? u.getPerfil().getNomePerfil() : "Sem Perfil"
                ))
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @Path("usuarios/alterar-perfil")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarPerfil(@QueryParam("usuarioId") Long usuarioId, @QueryParam("perfilId") Long perfilId) {
        usuarioBO.alterarPerfil(usuarioId, perfilId);
        return Response.ok("{\"mensagem\": \"Perfil alterado com sucesso.\"}").build();
    }
}