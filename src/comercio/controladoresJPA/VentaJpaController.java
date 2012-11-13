package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.ItemVenta;
import comercio.modelo.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getItems() == null) {
            venta.setItems(new ArrayList<ItemVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ItemVenta> attachedItems = new ArrayList<ItemVenta>();
            for (ItemVenta itemsItemVentaToAttach : venta.getItems()) {
                itemsItemVentaToAttach = em.getReference(itemsItemVentaToAttach.getClass(), itemsItemVentaToAttach.getId());
                attachedItems.add(itemsItemVentaToAttach);
            }
            venta.setItems(attachedItems);
            em.persist(venta);
            for (ItemVenta itemsItemVenta : venta.getItems()) {
                Venta oldVentaOfItemsItemVenta = itemsItemVenta.getVenta();
                itemsItemVenta.setVenta(venta);
                itemsItemVenta = em.merge(itemsItemVenta);
                if (oldVentaOfItemsItemVenta != null) {
                    oldVentaOfItemsItemVenta.getItems().remove(itemsItemVenta);
                    oldVentaOfItemsItemVenta = em.merge(oldVentaOfItemsItemVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getId());
            List<ItemVenta> itemsOld = persistentVenta.getItems();
            List<ItemVenta> itemsNew = venta.getItems();
            List<ItemVenta> attachedItemsNew = new ArrayList<ItemVenta>();
            for (ItemVenta itemsNewItemVentaToAttach : itemsNew) {
                itemsNewItemVentaToAttach = em.getReference(itemsNewItemVentaToAttach.getClass(), itemsNewItemVentaToAttach.getId());
                attachedItemsNew.add(itemsNewItemVentaToAttach);
            }
            itemsNew = attachedItemsNew;
            venta.setItems(itemsNew);
            venta = em.merge(venta);
            for (ItemVenta itemsOldItemVenta : itemsOld) {
                if (!itemsNew.contains(itemsOldItemVenta)) {
                    itemsOldItemVenta.setVenta(null);
                    itemsOldItemVenta = em.merge(itemsOldItemVenta);
                }
            }
            for (ItemVenta itemsNewItemVenta : itemsNew) {
                if (!itemsOld.contains(itemsNewItemVenta)) {
                    Venta oldVentaOfItemsNewItemVenta = itemsNewItemVenta.getVenta();
                    itemsNewItemVenta.setVenta(venta);
                    itemsNewItemVenta = em.merge(itemsNewItemVenta);
                    if (oldVentaOfItemsNewItemVenta != null && !oldVentaOfItemsNewItemVenta.equals(venta)) {
                        oldVentaOfItemsNewItemVenta.getItems().remove(itemsNewItemVenta);
                        oldVentaOfItemsNewItemVenta = em.merge(oldVentaOfItemsNewItemVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = venta.getId();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<ItemVenta> items = venta.getItems();
            for (ItemVenta itemsItemVenta : items) {
                itemsItemVenta.setVenta(null);
                itemsItemVenta = em.merge(itemsItemVenta);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
