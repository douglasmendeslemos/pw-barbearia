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

    public java.util.List<AgendamentoEntity> buscarPorBarbeiro(String barbeiroNome) {
        return entityManager.createQuery(
                "SELECT a FROM AgendamentoEntity a WHERE a.barbeiroNome = :barbeiroNome", 
                AgendamentoEntity.class)
                .setParameter("barbeiroNome", barbeiroNome)
                .getResultList();
    }

    public java.util.List<AgendamentoEntity> buscarPorClienteEmail(String email) {
        return entityManager.createQuery(
                "SELECT a FROM AgendamentoEntity a WHERE a.cliente.email = :email", 
                AgendamentoEntity.class)
                .setParameter("email", email)
                .getResultList();
    }
}
