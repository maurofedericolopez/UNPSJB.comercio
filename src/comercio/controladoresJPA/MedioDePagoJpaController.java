package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.MedioDePago;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro
 */
public class MedioDePagoJpaController implements Serializable {

    public MedioDePagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MedioDePago medioPago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medioPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MedioDePago medioPago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            medioPago = em.merge(medioPago);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = medioPago.getId();
                if (findMedioPago(id) == null) {
                    throw new NonexistentEntityException("The medioPago with id " + id + " no longer exists.");
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
            MedioDePago medioPago;
            try {
                medioPago = em.getReference(MedioDePago.class, id);
                medioPago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medioPago with id " + id + " no longer exists.", enfe);
            }
            em.remove(medioPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MedioDePago> findMedioPagoEntities() {
        return findMedioPagoEntities(true, -1, -1);
    }

    public List<MedioDePago> findMedioPagoEntities(int maxResults, int firstResult) {
        return findMedioPagoEntities(false, maxResults, firstResult);
    }

    private List<MedioDePago> findMedioPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MedioDePago.class));
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

    public MedioDePago findMedioPago(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedioDePago.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedioPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MedioDePago> rt = cq.from(MedioDePago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<MedioDePago> obtenerTodosLosMediosDePago() {
        ArrayList<MedioDePago> mediosDePago = new ArrayList();
        Object[] array = findMedioPagoEntities().toArray();
        for(Object o : array)
            mediosDePago.add((MedioDePago) o);
        return mediosDePago;
    }

}
