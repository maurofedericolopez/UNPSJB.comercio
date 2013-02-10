package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Origen;

/**
 *
 * @author Mauro Federico Lopez
 */
public class OrigenJpaController implements Serializable {

    public OrigenJpaController() {
        this.emf = ControllerSingleton.getEmf();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearOrigen(Origen origen) {
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

    public void editarOrigen(Origen origen) throws NonexistentEntityException, Exception {
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
                if (encontrarOrigen(id) == null) {
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

    public void destruirOrigen(Long id) throws NonexistentEntityException {
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

    public List<Origen> encontrarOrigenEntities() {
        return encontrarOrigenEntities(true, -1, -1);
    }

    public List<Origen> encontrarOrigenEntities(int maxResults, int firstResult) {
        return encontrarOrigenEntities(false, maxResults, firstResult);
    }

    private List<Origen> encontrarOrigenEntities(boolean all, int maxResults, int firstResult) {
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

    public Origen encontrarOrigen(Long id) {
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

    public ArrayList<Origen> obtenerTodosLosOrigen() {
        ArrayList<Origen> origenes = new ArrayList();
        Object[] array = encontrarOrigenEntities().toArray();
        for(Object o : array)
            origenes.add((Origen) o);
        return origenes;
    }

}
