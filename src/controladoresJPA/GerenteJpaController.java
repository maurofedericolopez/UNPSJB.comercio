package controladoresJPA;

import controladoresJPA.exceptions.NonexistentEntityException;
import modelo.Gerente;
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
public class GerenteJpaController implements Serializable {

    public GerenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gerente gerente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gerente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gerente gerente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gerente = em.merge(gerente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gerente.getId();
                if (findGerente(id) == null) {
                    throw new NonexistentEntityException("The gerente with id " + id + " no longer exists.");
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
            Gerente gerente;
            try {
                gerente = em.getReference(Gerente.class, id);
                gerente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gerente with id " + id + " no longer exists.", enfe);
            }
            em.remove(gerente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gerente> findGerenteEntities() {
        return findGerenteEntities(true, -1, -1);
    }

    public List<Gerente> findGerenteEntities(int maxResults, int firstResult) {
        return findGerenteEntities(false, maxResults, firstResult);
    }

    private List<Gerente> findGerenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gerente.class));
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

    public Gerente findGerente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gerente.class, id);
        } finally {
            em.close();
        }
    }

    public int getGerenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gerente> rt = cq.from(Gerente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Gerente findGestorInventarioByNombreUsuario(String nombreUsuario) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Gerente> c = cb.createQuery(Gerente.class);
            Root<Gerente> p = c.from(Gerente.class);
            c.select(p).where(cb.equal(p.get("nombreusuario"), nombreUsuario));
            Query q = em.createQuery(c);
            return (Gerente) q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
}
