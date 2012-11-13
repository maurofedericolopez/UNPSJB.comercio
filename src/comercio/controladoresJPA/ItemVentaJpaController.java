package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.ItemVenta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class ItemVentaJpaController implements Serializable {

    public ItemVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemVenta itemVenta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta venta = itemVenta.getVenta();
            if (venta != null) {
                venta = em.getReference(venta.getClass(), venta.getId());
                itemVenta.setVenta(venta);
            }
            em.persist(itemVenta);
            if (venta != null) {
                venta.getItems().add(itemVenta);
                venta = em.merge(venta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemVenta itemVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemVenta persistentItemVenta = em.find(ItemVenta.class, itemVenta.getId());
            Venta ventaOld = persistentItemVenta.getVenta();
            Venta ventaNew = itemVenta.getVenta();
            if (ventaNew != null) {
                ventaNew = em.getReference(ventaNew.getClass(), ventaNew.getId());
                itemVenta.setVenta(ventaNew);
            }
            itemVenta = em.merge(itemVenta);
            if (ventaOld != null && !ventaOld.equals(ventaNew)) {
                ventaOld.getItems().remove(itemVenta);
                ventaOld = em.merge(ventaOld);
            }
            if (ventaNew != null && !ventaNew.equals(ventaOld)) {
                ventaNew.getItems().add(itemVenta);
                ventaNew = em.merge(ventaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = itemVenta.getId();
                if (findItemVenta(id) == null) {
                    throw new NonexistentEntityException("The itemVenta with id " + id + " no longer exists.");
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
            ItemVenta itemVenta;
            try {
                itemVenta = em.getReference(ItemVenta.class, id);
                itemVenta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemVenta with id " + id + " no longer exists.", enfe);
            }
            Venta venta = itemVenta.getVenta();
            if (venta != null) {
                venta.getItems().remove(itemVenta);
                venta = em.merge(venta);
            }
            em.remove(itemVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemVenta> findItemVentaEntities() {
        return findItemVentaEntities(true, -1, -1);
    }

    public List<ItemVenta> findItemVentaEntities(int maxResults, int firstResult) {
        return findItemVentaEntities(false, maxResults, firstResult);
    }

    private List<ItemVenta> findItemVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemVenta.class));
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

    public ItemVenta findItemVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemVenta> rt = cq.from(ItemVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
