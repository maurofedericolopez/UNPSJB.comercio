package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Almacen;
import comercio.modelo.Lote;
import comercio.modelo.LoteAlmacenado;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.*;

/**
 *
 * @author Mauro
 */
public class LoteAlmacenadoJpaController implements Serializable {

    public LoteAlmacenadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LoteAlmacenado loteAlmacenado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen almacen = loteAlmacenado.getAlmacen();
            if (almacen != null) {
                almacen = em.getReference(almacen.getClass(), almacen.getId());
                loteAlmacenado.setAlmacen(almacen);
            }
            em.persist(loteAlmacenado);
            if (almacen != null) {
                almacen.getLotesAlmacenados().add(loteAlmacenado);
                almacen = em.merge(almacen);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LoteAlmacenado loteAlmacenado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteAlmacenado persistentLoteAlmacenado = em.find(LoteAlmacenado.class, loteAlmacenado.getId());
            Almacen almacenOld = persistentLoteAlmacenado.getAlmacen();
            Almacen almacenNew = loteAlmacenado.getAlmacen();
            if (almacenNew != null) {
                almacenNew = em.getReference(almacenNew.getClass(), almacenNew.getId());
                loteAlmacenado.setAlmacen(almacenNew);
            }
            loteAlmacenado = em.merge(loteAlmacenado);
            if (almacenOld != null && !almacenOld.equals(almacenNew)) {
                almacenOld.getLotesAlmacenados().remove(loteAlmacenado);
                almacenOld = em.merge(almacenOld);
            }
            if (almacenNew != null && !almacenNew.equals(almacenOld)) {
                almacenNew.getLotesAlmacenados().add(loteAlmacenado);
                almacenNew = em.merge(almacenNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteAlmacenado.getId();
                if (findLoteAlmacenado(id) == null) {
                    throw new NonexistentEntityException("The loteAlmacenado with id " + id + " no longer exists.");
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
            LoteAlmacenado loteAlmacenado;
            try {
                loteAlmacenado = em.getReference(LoteAlmacenado.class, id);
                loteAlmacenado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteAlmacenado with id " + id + " no longer exists.", enfe);
            }
            Almacen almacen = loteAlmacenado.getAlmacen();
            if (almacen != null) {
                almacen.getLotesAlmacenados().remove(loteAlmacenado);
                almacen = em.merge(almacen);
            }
            em.remove(loteAlmacenado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LoteAlmacenado> findLoteAlmacenadoEntities() {
        return findLoteAlmacenadoEntities(true, -1, -1);
    }

    public List<LoteAlmacenado> findLoteAlmacenadoEntities(int maxResults, int firstResult) {
        return findLoteAlmacenadoEntities(false, maxResults, firstResult);
    }

    private List<LoteAlmacenado> findLoteAlmacenadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteAlmacenado.class));
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

    public LoteAlmacenado findLoteAlmacenado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteAlmacenado.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoteAlmacenadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteAlmacenado> rt = cq.from(LoteAlmacenado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public LoteAlmacenado buscarLoteAlmacenadoPorAlmacenYLote(Almacen almacen, Lote lote) {
        Object[] array = almacen.getLotesAlmacenados().toArray();
        String codigoLote = lote.getCodigo();
        LoteAlmacenado loteAlmacenado = null;
        for(Object o : array) {
            LoteAlmacenado la = (LoteAlmacenado) o;
            if(la.getLote().getCodigo().equals(codigoLote)) {
                loteAlmacenado = la;
                break;
            }
        }
        return loteAlmacenado;
    }

}
