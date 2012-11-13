package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.Sucursal;
import comercio.modelo.ProductoEnVenta;
import comercio.modelo.PuntoVenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class PuntoVentaJpaController implements Serializable {

    public PuntoVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PuntoVenta puntoVenta) {
        if (puntoVenta.getProductosEnVenta() == null) {
            puntoVenta.setProductosEnVenta(new ArrayList<ProductoEnVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal sucursal = puntoVenta.getSucursal();
            if (sucursal != null) {
                sucursal = em.getReference(sucursal.getClass(), sucursal.getId());
                puntoVenta.setSucursal(sucursal);
            }
            List<ProductoEnVenta> attachedProductosEnVenta = new ArrayList<ProductoEnVenta>();
            for (ProductoEnVenta productosEnVentaProductoEnVentaToAttach : puntoVenta.getProductosEnVenta()) {
                productosEnVentaProductoEnVentaToAttach = em.getReference(productosEnVentaProductoEnVentaToAttach.getClass(), productosEnVentaProductoEnVentaToAttach.getId());
                attachedProductosEnVenta.add(productosEnVentaProductoEnVentaToAttach);
            }
            puntoVenta.setProductosEnVenta(attachedProductosEnVenta);
            em.persist(puntoVenta);
            if (sucursal != null) {
                sucursal.getPuntosDeVentas().add(puntoVenta);
                sucursal = em.merge(sucursal);
            }
            for (ProductoEnVenta productosEnVentaProductoEnVenta : puntoVenta.getProductosEnVenta()) {
                PuntoVenta oldPuntoVentaOfProductosEnVentaProductoEnVenta = productosEnVentaProductoEnVenta.getPuntoVenta();
                productosEnVentaProductoEnVenta.setPuntoVenta(puntoVenta);
                productosEnVentaProductoEnVenta = em.merge(productosEnVentaProductoEnVenta);
                if (oldPuntoVentaOfProductosEnVentaProductoEnVenta != null) {
                    oldPuntoVentaOfProductosEnVentaProductoEnVenta.getProductosEnVenta().remove(productosEnVentaProductoEnVenta);
                    oldPuntoVentaOfProductosEnVentaProductoEnVenta = em.merge(oldPuntoVentaOfProductosEnVentaProductoEnVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PuntoVenta puntoVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntoVenta persistentPuntoVenta = em.find(PuntoVenta.class, puntoVenta.getId());
            Sucursal sucursalOld = persistentPuntoVenta.getSucursal();
            Sucursal sucursalNew = puntoVenta.getSucursal();
            List<ProductoEnVenta> productosEnVentaOld = persistentPuntoVenta.getProductosEnVenta();
            List<ProductoEnVenta> productosEnVentaNew = puntoVenta.getProductosEnVenta();
            if (sucursalNew != null) {
                sucursalNew = em.getReference(sucursalNew.getClass(), sucursalNew.getId());
                puntoVenta.setSucursal(sucursalNew);
            }
            List<ProductoEnVenta> attachedProductosEnVentaNew = new ArrayList<ProductoEnVenta>();
            for (ProductoEnVenta productosEnVentaNewProductoEnVentaToAttach : productosEnVentaNew) {
                productosEnVentaNewProductoEnVentaToAttach = em.getReference(productosEnVentaNewProductoEnVentaToAttach.getClass(), productosEnVentaNewProductoEnVentaToAttach.getId());
                attachedProductosEnVentaNew.add(productosEnVentaNewProductoEnVentaToAttach);
            }
            productosEnVentaNew = attachedProductosEnVentaNew;
            puntoVenta.setProductosEnVenta(productosEnVentaNew);
            puntoVenta = em.merge(puntoVenta);
            if (sucursalOld != null && !sucursalOld.equals(sucursalNew)) {
                sucursalOld.getPuntosDeVentas().remove(puntoVenta);
                sucursalOld = em.merge(sucursalOld);
            }
            if (sucursalNew != null && !sucursalNew.equals(sucursalOld)) {
                sucursalNew.getPuntosDeVentas().add(puntoVenta);
                sucursalNew = em.merge(sucursalNew);
            }
            for (ProductoEnVenta productosEnVentaOldProductoEnVenta : productosEnVentaOld) {
                if (!productosEnVentaNew.contains(productosEnVentaOldProductoEnVenta)) {
                    productosEnVentaOldProductoEnVenta.setPuntoVenta(null);
                    productosEnVentaOldProductoEnVenta = em.merge(productosEnVentaOldProductoEnVenta);
                }
            }
            for (ProductoEnVenta productosEnVentaNewProductoEnVenta : productosEnVentaNew) {
                if (!productosEnVentaOld.contains(productosEnVentaNewProductoEnVenta)) {
                    PuntoVenta oldPuntoVentaOfProductosEnVentaNewProductoEnVenta = productosEnVentaNewProductoEnVenta.getPuntoVenta();
                    productosEnVentaNewProductoEnVenta.setPuntoVenta(puntoVenta);
                    productosEnVentaNewProductoEnVenta = em.merge(productosEnVentaNewProductoEnVenta);
                    if (oldPuntoVentaOfProductosEnVentaNewProductoEnVenta != null && !oldPuntoVentaOfProductosEnVentaNewProductoEnVenta.equals(puntoVenta)) {
                        oldPuntoVentaOfProductosEnVentaNewProductoEnVenta.getProductosEnVenta().remove(productosEnVentaNewProductoEnVenta);
                        oldPuntoVentaOfProductosEnVentaNewProductoEnVenta = em.merge(oldPuntoVentaOfProductosEnVentaNewProductoEnVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = puntoVenta.getId();
                if (findPuntoVenta(id) == null) {
                    throw new NonexistentEntityException("The puntoVenta with id " + id + " no longer exists.");
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
            PuntoVenta puntoVenta;
            try {
                puntoVenta = em.getReference(PuntoVenta.class, id);
                puntoVenta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntoVenta with id " + id + " no longer exists.", enfe);
            }
            Sucursal sucursal = puntoVenta.getSucursal();
            if (sucursal != null) {
                sucursal.getPuntosDeVentas().remove(puntoVenta);
                sucursal = em.merge(sucursal);
            }
            List<ProductoEnVenta> productosEnVenta = puntoVenta.getProductosEnVenta();
            for (ProductoEnVenta productosEnVentaProductoEnVenta : productosEnVenta) {
                productosEnVentaProductoEnVenta.setPuntoVenta(null);
                productosEnVentaProductoEnVenta = em.merge(productosEnVentaProductoEnVenta);
            }
            em.remove(puntoVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PuntoVenta> findPuntoVentaEntities() {
        return findPuntoVentaEntities(true, -1, -1);
    }

    public List<PuntoVenta> findPuntoVentaEntities(int maxResults, int firstResult) {
        return findPuntoVentaEntities(false, maxResults, firstResult);
    }

    private List<PuntoVenta> findPuntoVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PuntoVenta.class));
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

    public PuntoVenta findPuntoVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuntoVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntoVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PuntoVenta> rt = cq.from(PuntoVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
