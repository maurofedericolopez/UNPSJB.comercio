package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.GestorInventario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro Federico Lopez
 */
public class GestorInventarioJpaController implements Serializable {

    public GestorInventarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GestorInventario gestorInventario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gestorInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GestorInventario gestorInventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gestorInventario = em.merge(gestorInventario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gestorInventario.getId();
                if (findGestorInventario(id) == null) {
                    throw new NonexistentEntityException("The gestorInventario with id " + id + " no longer exists.");
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
            GestorInventario gestorInventario;
            try {
                gestorInventario = em.getReference(GestorInventario.class, id);
                gestorInventario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gestorInventario with id " + id + " no longer exists.", enfe);
            }
            em.remove(gestorInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GestorInventario> findGestorInventarioEntities() {
        return findGestorInventarioEntities(true, -1, -1);
    }

    public List<GestorInventario> findGestorInventarioEntities(int maxResults, int firstResult) {
        return findGestorInventarioEntities(false, maxResults, firstResult);
    }

    private List<GestorInventario> findGestorInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestorInventario.class));
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

    public GestorInventario findGestorInventario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestorInventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestorInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestorInventario> rt = cq.from(GestorInventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public GestorInventario findGestorInventarioByNombreUsuario(String nombreUsuario) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<GestorInventario> c = cb.createQuery(GestorInventario.class);
            Root<GestorInventario> p = c.from(GestorInventario.class);
            c.select(p).where(cb.equal(p.get("nombreusuario"), nombreUsuario));
            Query q = em.createQuery(c);
            return (GestorInventario) q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
}
