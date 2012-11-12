package comercio.controladoresJPA;

import comercio.exceptions.NonexistentEntityException;
import comercio.modelo.PrecioAnterior;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro Federico Lopez
 */
public class PrecioAnteriorJpaController implements Serializable {

    public PrecioAnteriorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PrecioAnterior precioAnterior) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(precioAnterior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PrecioAnterior precioAnterior) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            precioAnterior = em.merge(precioAnterior);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = precioAnterior.getId();
                if (findPrecioAnterior(id) == null) {
                    throw new NonexistentEntityException("The precioAnterior with id " + id + " no longer exists.");
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
            PrecioAnterior precioAnterior;
            try {
                precioAnterior = em.getReference(PrecioAnterior.class, id);
                precioAnterior.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The precioAnterior with id " + id + " no longer exists.", enfe);
            }
            em.remove(precioAnterior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PrecioAnterior> findPrecioAnteriorEntities() {
        return findPrecioAnteriorEntities(true, -1, -1);
    }

    public List<PrecioAnterior> findPrecioAnteriorEntities(int maxResults, int firstResult) {
        return findPrecioAnteriorEntities(false, maxResults, firstResult);
    }

    private List<PrecioAnterior> findPrecioAnteriorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrecioAnterior.class));
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

    public PrecioAnterior findPrecioAnterior(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrecioAnterior.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrecioAnteriorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrecioAnterior> rt = cq.from(PrecioAnterior.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
