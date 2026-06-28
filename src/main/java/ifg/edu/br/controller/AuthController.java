package ifg.edu.br.controller;


import ifg.edu.br.model.bo.UsuarioBO;
import ifg.edu.br.model.dto.AuthResultadoDTO;
import ifg.edu.br.model.dto.LoginRequestDTO;
import ifg.edu.br.model.dto.LoginResponseDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/login")
public class AuthController {

    @Inject
     UsuarioBO usuarioBO;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance login() { return Templates.login();}

    @Path("/auth")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequestDTO loginRequestDTO) {

        AuthResultadoDTO authResultado = usuarioBO.realizarLogin(loginRequestDTO);


        NewCookie jwtCookie = new NewCookie.Builder("jwt")
                .value(authResultado.token())
                .path("/") // Válido para toda aplicação
                .httpOnly(true)  //Impede acesso via java script
                .secure(false) // Desabilitado para testes locais (HTTP)
                .sameSite(NewCookie.SameSite.STRICT)
                .maxAge(3600)
                .build();

        //Dto limpo para o front sem o token
        LoginResponseDTO responseDTO = new LoginResponseDTO(
                authResultado.nome(),
                authResultado.perfil()
        );

        // retorna o status de ok, com o cookie no body

        return Response.ok(responseDTO)
                .cookie(jwtCookie)
                .build();

    }

}
