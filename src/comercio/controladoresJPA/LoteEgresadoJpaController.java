package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.Egreso;
import comercio.modelo.LoteEgresado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class LoteEgresadoJpaController implements Serializable {

    public LoteEgresadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LoteEgresado loteEgresado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso egreso = loteEgresado.getEgreso();
            if (egreso != null) {
                egreso = em.getReference(egreso.getClass(), egreso.getId());
                loteEgresado.setEgreso(egreso);
            }
            em.persist(loteEgresado);
            if (egreso != null) {
                egreso.getLotesEgresados().add(loteEgresado);
                egreso = em.merge(egreso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LoteEgresado loteEgresado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteEgresado persistentLoteEgresado = em.find(LoteEgresado.class, loteEgresado.getId());
            Egreso egresoOld = persistentLoteEgresado.getEgreso();
            Egreso egresoNew = loteEgresado.getEgreso();
            if (egresoNew != null) {
                egresoNew = em.getReference(egresoNew.getClass(), egresoNew.getId());
                loteEgresado.setEgreso(egresoNew);
            }
            loteEgresado = em.merge(loteEgresado);
            if (egresoOld != null && !egresoOld.equals(egresoNew)) {
                egresoOld.getLotesEgresados().remove(loteEgresado);
                egresoOld = em.merge(egresoOld);
            }
            if (egresoNew != null && !egresoNew.equals(egresoOld)) {
                egresoNew.getLotesEgresados().add(loteEgresado);
                egresoNew = em.merge(egresoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteEgresado.getId();
                if (findLoteEgresado(id) == null) {
                    throw new NonexistentEntityException("The loteEgresado with id " + id + " no longer exists.");
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
            LoteEgresado loteEgresado;
            try {
                loteEgresado = em.getReference(LoteEgresado.class, id);
                loteEgresado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteEgresado with id " + id + " no longer exists.", enfe);
            }
            Egreso egreso = loteEgresado.getEgreso();
            if (egreso != null) {
                egreso.getLotesEgresados().remove(loteEgresado);
                egreso = em.merge(egreso);
            }
            em.remove(loteEgresado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LoteEgresado> findLoteEgresadoEntities() {
        return findLoteEgresadoEntities(true, -1, -1);
    }

    public List<LoteEgresado> findLoteEgresadoEntities(int maxResults, int firstResult) {
        return findLoteEgresadoEntities(false, maxResults, firstResult);
    }

    private List<LoteEgresado> findLoteEgresadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteEgresado.class));
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

    public LoteEgresado findLoteEgresado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteEgresado.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoteEgresadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteEgresado> rt = cq.from(LoteEgresado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
