package ifg.edu.br.model.dao;

import ifg.edu.br.model.entity.AuditoriaEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class AuditoriaDAO {

    @Inject
    EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void registrar(String tipoEvento, String email, String descricao) {
        try {
            AuditoriaEntity log = new AuditoriaEntity();
            log.setTipoEvento(tipoEvento);
            log.setEmail(email);
            log.setDescricao(descricao);
            log.setDataHora(LocalDateTime.now());
            entityManager.persist(log);
        } catch (Exception e) {
            System.err.println("Erro ao registrar auditoria de evento: " + e.getMessage());
        }
    }
}
