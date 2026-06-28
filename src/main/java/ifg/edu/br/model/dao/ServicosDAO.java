package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.ServicosEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ServicosDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(ServicosEntity servico) {
        entityManager.persist(servico);
    }
}
