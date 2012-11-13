package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Origen;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro
 */
public class OrigenJpaController implements Serializable {

    public OrigenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Origen origen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(origen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Origen origen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            origen = em.merge(origen);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = origen.getId();
                if (findOrigen(id) == null) {
                    throw new NonexistentEntityException("The origen with id " + id + " no longer exists.");
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
            Origen origen;
            try {
                origen = em.getReference(Origen.class, id);
                origen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The origen with id " + id + " no longer exists.", enfe);
            }
            em.remove(origen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Origen> findOrigenEntities() {
        return findOrigenEntities(true, -1, -1);
    }

    public List<Origen> findOrigenEntities(int maxResults, int firstResult) {
        return findOrigenEntities(false, maxResults, firstResult);
    }

    private List<Origen> findOrigenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Origen.class));
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

    public Origen findOrigen(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Origen.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrigenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Origen> rt = cq.from(Origen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
