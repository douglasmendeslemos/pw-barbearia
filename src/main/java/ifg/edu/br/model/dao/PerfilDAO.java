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

    public List<PerfilEntity> listAll(){
        //language=jpql
        String hql = "SELECT p FROM PerfilEntity p";

        return entityManager
                .createQuery(hql)
                .getResultList();
    }

    public List<String> listAllNames(){
        //language=hql
        String hql = "SELECT p.nome FROM PerfilEntity p";
        return entityManager
                .createQuery(hql)
                .getResultList();
    }


}

