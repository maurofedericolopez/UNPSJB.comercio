package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.ProductoEnVenta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.PuntoVenta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class ProductoEnVentaJpaController implements Serializable {

    public ProductoEnVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductoEnVenta productoEnVenta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PuntoVenta puntoVenta = productoEnVenta.getPuntoVenta();
            if (puntoVenta != null) {
                puntoVenta = em.getReference(puntoVenta.getClass(), puntoVenta.getId());
                productoEnVenta.setPuntoVenta(puntoVenta);
            }
            em.persist(productoEnVenta);
            if (puntoVenta != null) {
                puntoVenta.getProductosEnVenta().add(productoEnVenta);
                puntoVenta = em.merge(puntoVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductoEnVenta productoEnVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductoEnVenta persistentProductoEnVenta = em.find(ProductoEnVenta.class, productoEnVenta.getId());
            PuntoVenta puntoVentaOld = persistentProductoEnVenta.getPuntoVenta();
            PuntoVenta puntoVentaNew = productoEnVenta.getPuntoVenta();
            if (puntoVentaNew != null) {
                puntoVentaNew = em.getReference(puntoVentaNew.getClass(), puntoVentaNew.getId());
                productoEnVenta.setPuntoVenta(puntoVentaNew);
            }
            productoEnVenta = em.merge(productoEnVenta);
            if (puntoVentaOld != null && !puntoVentaOld.equals(puntoVentaNew)) {
                puntoVentaOld.getProductosEnVenta().remove(productoEnVenta);
                puntoVentaOld = em.merge(puntoVentaOld);
            }
            if (puntoVentaNew != null && !puntoVentaNew.equals(puntoVentaOld)) {
                puntoVentaNew.getProductosEnVenta().add(productoEnVenta);
                puntoVentaNew = em.merge(puntoVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = productoEnVenta.getId();
                if (findProductoEnVenta(id) == null) {
                    throw new NonexistentEntityException("The productoEnVenta with id " + id + " no longer exists.");
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
            ProductoEnVenta productoEnVenta;
            try {
                productoEnVenta = em.getReference(ProductoEnVenta.class, id);
                productoEnVenta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productoEnVenta with id " + id + " no longer exists.", enfe);
            }
            PuntoVenta puntoVenta = productoEnVenta.getPuntoVenta();
            if (puntoVenta != null) {
                puntoVenta.getProductosEnVenta().remove(productoEnVenta);
                puntoVenta = em.merge(puntoVenta);
            }
            em.remove(productoEnVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductoEnVenta> findProductoEnVentaEntities() {
        return findProductoEnVentaEntities(true, -1, -1);
    }

    public List<ProductoEnVenta> findProductoEnVentaEntities(int maxResults, int firstResult) {
        return findProductoEnVentaEntities(false, maxResults, firstResult);
    }

    private List<ProductoEnVenta> findProductoEnVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductoEnVenta.class));
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

    public ProductoEnVenta findProductoEnVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoEnVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoEnVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductoEnVenta> rt = cq.from(ProductoEnVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
