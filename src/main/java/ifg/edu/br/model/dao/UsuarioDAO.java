package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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

    public UsuarioEntity buscarPorEmail(String email) {
        //language=hql
        String hql = "SELECT u FROM UsuarioEntity u WHERE u.email = :email";
        return (UsuarioEntity) entityManager
                .createQuery(hql)
                .setParameter("email", email)
                .getResultStream().findFirst() //Retorna primeira opção encontrada ou null
                .orElse(null);
    }

    public java.util.List<UsuarioEntity> listarTodos() {
        return entityManager.createQuery("SELECT u FROM UsuarioEntity u", UsuarioEntity.class).getResultList();
    }

    public UsuarioEntity buscarPorId(Long id) {
        return entityManager.find(UsuarioEntity.class, id);
    }

    @Transactional
    public void atualizar(UsuarioEntity usuario) {
        entityManager.merge(usuario);
    }
}
