package ifg.edu.br.controller;

import ifg.edu.br.model.bo.UsuarioBO;
import ifg.edu.br.model.dao.AuditoriaDAO;
import ifg.edu.br.model.dto.AuthResultadoDTO;
import ifg.edu.br.model.dto.LoginRequestDTO;
import ifg.edu.br.model.dto.LoginResponseDTO;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/auth")
public class AuthController {

    @Inject
    UsuarioBO usuarioBO;

    @Inject
    AuditoriaDAO auditoriaDAO;

    @Inject
    JsonWebToken jwt;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance login() {
        return Templates.login();
    }

    @Path("/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequestDTO) {
        try {
            AuthResultadoDTO authResultado = usuarioBO.realizarLogin(loginRequestDTO);

            // Registrar sucesso no log de auditoria
            auditoriaDAO.registrar("LOGIN_SUCESSO", loginRequestDTO.email(), 
                "Login realizado com sucesso. Perfil: " + authResultado.perfil());

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

        } catch (Exception e) {
            // Registrar falha no log de auditoria
            auditoriaDAO.registrar("LOGIN_FALHA", loginRequestDTO.email(), 
                "Tentativa de login falhou. Motivo: " + e.getMessage());
            throw e;
        }
    }

    @Path("/logout")
    @POST
    public Response logout() {
        String email = (jwt != null && jwt.getName() != null) ? jwt.getName() : "desconhecido";
        
        // Registrar logout no log de auditoria
        auditoriaDAO.registrar("LOGOUT", email, "Logout realizado com sucesso.");

        NewCookie cookie = new NewCookie.Builder("jwt")
                .value("")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite(NewCookie.SameSite.STRICT)
                .maxAge(0)
                .build();

        return Response.ok()
                .cookie(cookie)
                .build();
    }
}
