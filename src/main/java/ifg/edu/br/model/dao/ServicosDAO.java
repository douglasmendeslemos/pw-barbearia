package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.ServicosEntity;
import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ServicosDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(ServicosEntity servico) {
        entityManager.persist(servico);
    }

    public List<ServicosEntity> listarTodos() {
        return entityManager.createQuery("SELECT s FROM ServicosEntity s", ServicosEntity.class).getResultList();
    }
}
