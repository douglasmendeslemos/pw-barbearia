package ifg.edu.br.controller;


import ifg.edu.br.model.bo.UsuarioBO;
import ifg.edu.br.model.dto.AuthResultadoDTO;
import ifg.edu.br.model.dto.LoginRequestDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    @Inject
    private UsuarioBO usuarioBO;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance login() {
        return LoginController.Templates.login();
    }

    @Path("/login")
    @POST()
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequestDTO loginRequestDTO) {
        AuthResultadoDTO resultadoAtenticacao = usuarioBO.realizarLogin(loginRequestDTO);
    }
}
