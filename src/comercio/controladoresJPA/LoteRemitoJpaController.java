package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.LoteRemito;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.Remito;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class LoteRemitoJpaController implements Serializable {

    public LoteRemitoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LoteRemito loteRemito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito remito = loteRemito.getRemito();
            if (remito != null) {
                remito = em.getReference(remito.getClass(), remito.getId());
                loteRemito.setRemito(remito);
            }
            em.persist(loteRemito);
            if (remito != null) {
                remito.getLotes().add(loteRemito);
                remito = em.merge(remito);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LoteRemito loteRemito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteRemito persistentLoteRemito = em.find(LoteRemito.class, loteRemito.getId());
            Remito remitoOld = persistentLoteRemito.getRemito();
            Remito remitoNew = loteRemito.getRemito();
            if (remitoNew != null) {
                remitoNew = em.getReference(remitoNew.getClass(), remitoNew.getId());
                loteRemito.setRemito(remitoNew);
            }
            loteRemito = em.merge(loteRemito);
            if (remitoOld != null && !remitoOld.equals(remitoNew)) {
                remitoOld.getLotes().remove(loteRemito);
                remitoOld = em.merge(remitoOld);
            }
            if (remitoNew != null && !remitoNew.equals(remitoOld)) {
                remitoNew.getLotes().add(loteRemito);
                remitoNew = em.merge(remitoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteRemito.getId();
                if (findLoteRemito(id) == null) {
                    throw new NonexistentEntityException("The loteRemito with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteRemito loteRemito;
            try {
                loteRemito = em.getReference(LoteRemito.class, id);
                loteRemito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteRemito with id " + id + " no longer exists.", enfe);
            }
            Remito remito = loteRemito.getRemito();
            if (remito != null) {
                remito.getLotes().remove(loteRemito);
                remito = em.merge(remito);
            }
            em.remove(loteRemito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LoteRemito> findLoteRemitoEntities() {
        return findLoteRemitoEntities(true, -1, -1);
    }

    public List<LoteRemito> findLoteRemitoEntities(int maxResults, int firstResult) {
        return findLoteRemitoEntities(false, maxResults, firstResult);
    }

    private List<LoteRemito> findLoteRemitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteRemito.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public LoteRemito findLoteRemito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteRemito.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoteRemitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteRemito> rt = cq.from(LoteRemito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
