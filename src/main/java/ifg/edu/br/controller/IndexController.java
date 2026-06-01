package ifg.edu.br.controller;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/")
public class IndexController {

    @CheckedTemplate
    public static class Templates{
        public static native TemplateInstance Login();
    }

//    @GET
//
//    @Produces(MediaType.TEXT_PLAIN)
//    public String teste() {
//        return "ok";
//    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance login(){//é apenas um metodo
        //apenas a representação do login, o direcionamento da pagina.
        return Templates.Login();
    }

}
