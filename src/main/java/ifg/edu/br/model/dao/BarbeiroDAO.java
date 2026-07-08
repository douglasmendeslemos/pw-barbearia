package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.BarbeiroEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BarbeiroDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(BarbeiroEntity barbeiro) {
        entityManager.persist(barbeiro);
    }

    public List<BarbeiroEntity> listarTodos() {
        return entityManager.createQuery("SELECT b FROM BarbeiroEntity b", BarbeiroEntity.class).getResultList();
    }
}
