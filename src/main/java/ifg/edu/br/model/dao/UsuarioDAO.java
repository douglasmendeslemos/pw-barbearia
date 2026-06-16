package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioDAO {


    @Inject
    EntityManager entityManager;

    @Transactional
    public void salvar(UsuarioEntity usuario) {
        entityManager.persist(usuario);
    }

    public boolean existeEmail(String email) {
        Long total = entityManager
                .createQuery("select count(u) from UsuarioEntity u where lower(u.email) = lower(:email)", Long.class)
                .setParameter("email", email)
                .getSingleResult();

        return total > 0;
    }
}
