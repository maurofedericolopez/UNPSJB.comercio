package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Producto;
import comercio.modelo.ProductoEnVenta;
import comercio.modelo.PuntoVenta;
import comercio.modelo.Sucursal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class PuntoVentaJpaController implements Serializable {

    public PuntoVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearPuntoDeVenta(PuntoVenta puntoVenta) {
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

    public void crearProductoEnVenta(ProductoEnVenta productoEnVenta) {
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

    public void editarPuntoDeVenta(PuntoVenta puntoVenta) throws NonexistentEntityException, Exception {
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
                if (encontrarPuntoVenta(id) == null) {
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

    public void editarProductoEnVenta(ProductoEnVenta productoEnVenta) throws NonexistentEntityException, Exception {
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
                if (encontrarProductoEnVenta(id) == null) {
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

    public void destruirPuntoDeVenta(Long id) throws NonexistentEntityException {
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

    public void destruirProductoEnVenta(Long id) throws NonexistentEntityException {
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

    public List<PuntoVenta> encontrarPuntoVentaEntities() {
        return encontrarPuntoVentaEntities(true, -1, -1);
    }

    public List<ProductoEnVenta> encontrarProductoEnVentaEntities() {
        return encontrarProductoEnVentaEntities(true, -1, -1);
    }

    public List<PuntoVenta> encontrarPuntoVentaEntities(int maxResults, int firstResult) {
        return encontrarPuntoVentaEntities(false, maxResults, firstResult);
    }

    public List<ProductoEnVenta> encontrarProductoEnVentaEntities(int maxResults, int firstResult) {
        return encontrarProductoEnVentaEntities(false, maxResults, firstResult);
    }

    private List<PuntoVenta> encontrarPuntoVentaEntities(boolean all, int maxResults, int firstResult) {
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

    private List<ProductoEnVenta> encontrarProductoEnVentaEntities(boolean all, int maxResults, int firstResult) {
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

    public PuntoVenta encontrarPuntoVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PuntoVenta.class, id);
        } finally {
            em.close();
        }
    }

    public ProductoEnVenta encontrarProductoEnVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductoEnVenta.class, id);
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

    public ArrayList<PuntoVenta> obtenerTodosLosPuntosDeVenta() {
        ArrayList<PuntoVenta> puntosDeVenta = new ArrayList();
        Object[] array = encontrarPuntoVentaEntities().toArray();
        for(Object o : array)
            puntosDeVenta.add((PuntoVenta) o);
        return puntosDeVenta;
    }

    public void descontarDePuntoDeVenta(PuntoVenta puntoDeVenta, String codigoProducto, Double cantidad) throws Exception {
        Object[] array = puntoDeVenta.getProductosEnVenta().toArray();
        Boolean encontrado = false;
        for(Object o : array) {
            ProductoEnVenta productoEnVenta = (ProductoEnVenta) o;
            if(productoEnVenta.getProducto().getCodigo().equals(codigoProducto)) {
                if(productoEnVenta.getCantidad() >= cantidad) {
                    Double cantidadNueva = productoEnVenta.getCantidad() - cantidad;
                    productoEnVenta.setCantidad(cantidadNueva);
                    encontrado = true;
                    editarProductoEnVenta(productoEnVenta);
                    break;
                } else {
                    throw new Exception("No hay stock para satisfacer la venta.");
                }
            }
        }
        if(!encontrado)
            throw new Exception("El punto de venta no registra el producto.");
    }

    public void aumentarStockEnVenta(PuntoVenta puntoDeVenta, Producto producto, Double cantidad) throws Exception {
        Object[] array = puntoDeVenta.getProductosEnVenta().toArray();
        Boolean encontrado = false;
        for(Object o : array) {
            ProductoEnVenta pev = (ProductoEnVenta) o;
            if(pev.getProducto().getCodigo().equals(producto.getCodigo())) {
                pev.setCantidad(pev.getCantidad() + cantidad);
                encontrado = true;
                editarProductoEnVenta(pev);
                break;
            }
        }
        if(!encontrado) {
            ProductoEnVenta nuevo = new ProductoEnVenta();
            nuevo.setProducto(producto);
            nuevo.setPuntoVenta(puntoDeVenta);
            nuevo.setCantidad(cantidad);
            crearProductoEnVenta(nuevo);
        }
    }

}
