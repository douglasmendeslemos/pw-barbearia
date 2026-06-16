package ifg.edu.br.model.bo;

import ifg.edu.br.model.dao.PerfilDAO;
import ifg.edu.br.model.entity.PerfilEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class PerfilBO {

    @Inject
    PerfilDAO perfilDAO;

    public Response salvar(String nome){
        PerfilEntity entity = new PerfilEntity();
        entity.setNomePerfil(nome);
        perfilDAO.save(entity);

        if (entity.getId() != null && entity.getId() > 0)
            return Response
                    .ok(entity)
                    .build();

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("Erro ao salvar")
                .build();
    }

    public Response listarTodos(){
        return Response.ok(perfilDAO.listAllNames()).build();
    }

}
