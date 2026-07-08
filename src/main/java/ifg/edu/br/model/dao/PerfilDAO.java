package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.PerfilEntity;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class PerfilDAO {

    @Inject
    private EntityManager entityManager;

    @Transactional
    public void save(PerfilEntity entity){
        entityManager.persist(entity);
    }

    public PerfilEntity findById(Long id){
        return entityManager.find(PerfilEntity.class, id);
    }

    public List<String> listAllNames(){
        //language=hql
        String hql = "SELECT p.nomePerfil FROM PerfilEntity p";
        return entityManager
                .createQuery(hql)
                .getResultList();
    }

    public List<PerfilEntity> listAll(){
        String hql = "SELECT p FROM PerfilEntity p";
        return entityManager
                .createQuery(hql, PerfilEntity.class)
                .getResultList();
    }

    public PerfilEntity buscarPorNome(String nomePerfil) {
        String hql = "SELECT p FROM PerfilEntity p WHERE LOWER(p.nomePerfil) = LOWER(:nome)";
        return entityManager
                .createQuery(hql, PerfilEntity.class)
                .setParameter("nome", nomePerfil)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
