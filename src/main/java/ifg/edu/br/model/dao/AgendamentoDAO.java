package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.AgendamentoEntity;
import ifg.edu.br.model.entity.ServicosEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AgendamentoDAO {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(AgendamentoEntity agendamentoEntity) {
        entityManager.persist(agendamentoEntity);
    }
}
