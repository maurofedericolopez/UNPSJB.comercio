package comercio.controladoresJPA;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.ItemVenta;
import comercio.modelo.Venta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
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
public class VentaJpaController extends Observable implements Serializable {

    private EntityManagerFactory emf = null;
    private MedioDePagoJpaController medioDePagoJpaController;
    private Venta venta = null;
    private ArrayList<ItemVenta> itemsDeVenta = new ArrayList();

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        medioDePagoJpaController = ControllerSingleton.getMedioDePagoJpaController();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearVenta(Venta venta) {
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

    public void crearItemVenta(ItemVenta itemVenta) {
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

    public void editarVenta(Venta venta) throws NonexistentEntityException, Exception {
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
                if (encontrarVenta(id) == null) {
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

    public void editarItemVenta(ItemVenta itemVenta) throws NonexistentEntityException, Exception {
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
                if (encontrarItemVenta(id) == null) {
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

    public void destruirVenta(Long id) throws NonexistentEntityException {
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

    public void destruirItemVenta(Long id) throws NonexistentEntityException {
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

    public List<Venta> encontrarVentaEntities() {
        return encontrarVentaEntities(true, -1, -1);
    }

    public List<ItemVenta> encontrarItemVentaEntities() {
        return encontrarItemVentaEntities(true, -1, -1);
    }

    public List<Venta> encontrarVentaEntities(int maxResults, int firstResult) {
        return encontrarVentaEntities(false, maxResults, firstResult);
    }

    public List<ItemVenta> encontrarItemVentaEntities(int maxResults, int firstResult) {
        return encontrarItemVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> encontrarVentaEntities(boolean all, int maxResults, int firstResult) {
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

    private List<ItemVenta> encontrarItemVentaEntities(boolean all, int maxResults, int firstResult) {
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

    public Venta encontrarVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public ItemVenta encontrarItemVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemVenta.class, id);
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

    public void agregarItemDeVenta(ItemVenta itemDeVenta) {
        String codigoBuscado = itemDeVenta.getProducto().getCodigo();
        Iterator<ItemVenta> i = getItemsDeVenta().iterator();
        Boolean encontrado = false;
        while(i.hasNext()) {
            ItemVenta item = i.next();
            if(item.getProducto().getCodigo().equals(codigoBuscado)) {
                Double cantidad = item.getCantidad() + itemDeVenta.getCantidad();
                item.setCantidad(cantidad);
                encontrado = true;
                break;
            }
        }
        if(encontrado == false)
            getItemsDeVenta().add(itemDeVenta);
        notificarCambios();
    }

    public void eliminarItemDeVenta(int index) throws Exception {
        if(index >= 0) {
            getItemsDeVenta().remove(index);
            notificarCambios();
        } else {
            throw new Exception("No ha seleccionado ningún item.");
        }
    }

    public void cancelarVenta() {
        venta = new Venta();
        itemsDeVenta = new ArrayList();
        notificarCambios();
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    /**
     * @return the itemsDeVenta
     */
    public ArrayList<ItemVenta> getItemsDeVenta() {
        return itemsDeVenta;
    }

}
