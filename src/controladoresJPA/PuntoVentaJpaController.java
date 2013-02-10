package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.CodigoProductoNoRegistradoException;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;
import modelo.Producto;
import modelo.ProductoEnVenta;
import modelo.PuntoVenta;
import modelo.Sucursal;

/**
 *
 * @author Mauro Federico Lopez
 */
public class PuntoVentaJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    private ProductoJpaController productoJpaController;

    public PuntoVentaJpaController() {
        this.emf = ControllerSingleton.getEmf();
        productoJpaController = ControllerSingleton.getProductoJpaController();
    }

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
            List<ProductoEnVenta> attachedProductosEnVenta = new ArrayList();
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
                PuntoVenta oldPuntoVentaOfProductosEnVentaProductoEnVenta = productosEnVentaProductoEnVenta.getPuntoDeVenta();
                productosEnVentaProductoEnVenta.setPuntoDeVenta(puntoVenta);
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
            PuntoVenta puntoVenta = productoEnVenta.getPuntoDeVenta();
            if (puntoVenta != null) {
                puntoVenta = em.getReference(puntoVenta.getClass(), puntoVenta.getId());
                productoEnVenta.setPuntoDeVenta(puntoVenta);
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
            List<ProductoEnVenta> attachedProductosEnVentaNew = new ArrayList();
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
                    productosEnVentaOldProductoEnVenta.setPuntoDeVenta(null);
                    productosEnVentaOldProductoEnVenta = em.merge(productosEnVentaOldProductoEnVenta);
                }
            }
            for (ProductoEnVenta productosEnVentaNewProductoEnVenta : productosEnVentaNew) {
                if (!productosEnVentaOld.contains(productosEnVentaNewProductoEnVenta)) {
                    PuntoVenta oldPuntoVentaOfProductosEnVentaNewProductoEnVenta = productosEnVentaNewProductoEnVenta.getPuntoDeVenta();
                    productosEnVentaNewProductoEnVenta.setPuntoDeVenta(puntoVenta);
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
            PuntoVenta puntoVentaOld = persistentProductoEnVenta.getPuntoDeVenta();
            PuntoVenta puntoVentaNew = productoEnVenta.getPuntoDeVenta();
            if (puntoVentaNew != null) {
                puntoVentaNew = em.getReference(puntoVentaNew.getClass(), puntoVentaNew.getId());
                productoEnVenta.setPuntoDeVenta(puntoVentaNew);
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
                productosEnVentaProductoEnVenta.setPuntoDeVenta(null);
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
            PuntoVenta puntoVenta = productoEnVenta.getPuntoDeVenta();
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

    public void descontarDePuntoDeVenta(PuntoVenta puntoDeVenta, Producto producto, Double cantidad) throws Exception {
        ProductoEnVenta productoEnVenta = buscarProductoEnVenta(puntoDeVenta, producto);
        if (productoEnVenta != null) {
            if (productoEnVenta.getCantidad() >= cantidad) {
                Double cantidadNueva = productoEnVenta.getCantidad() - cantidad;
                productoEnVenta.setCantidad(cantidadNueva);
                editarProductoEnVenta(productoEnVenta);
            } else {
                throw new Exception("No hay stock para satisfacer la venta.");
            }
        } else {
            throw new Exception("El punto de venta no registra el producto.");
        }
    }

    public void aumentarStockEnVenta(PuntoVenta puntoDeVenta, Producto producto, Double cantidad) throws Exception {
        ProductoEnVenta productoEnVenta = buscarProductoEnVenta(puntoDeVenta, producto);
        if(productoEnVenta != null) {
            Double nuevaCantidad = productoEnVenta.getCantidad() + cantidad;
            productoEnVenta.setCantidad(nuevaCantidad);
            editarProductoEnVenta(productoEnVenta);
        } else {
            productoEnVenta = new ProductoEnVenta();
            productoEnVenta.setPuntoDeVenta(puntoDeVenta);
            productoEnVenta.setProducto(producto);
            productoEnVenta.setCantidad(cantidad);
            crearProductoEnVenta(productoEnVenta);
        }
    }

    public ProductoEnVenta buscarProductoEnVenta(PuntoVenta puntoDeVenta, Producto producto) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductoEnVenta> cq = cb.createQuery(ProductoEnVenta.class);
            Root<ProductoEnVenta> root = cq.from(ProductoEnVenta.class);
            cq.select(root);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate puntoDeVentaPredicate, productoPredicate;

            if (puntoDeVenta != null) {
                puntoDeVentaPredicate = cb.equal(root.get("puntoDeVenta"), puntoDeVenta);
                predicateList.add(puntoDeVentaPredicate);
            }

            if (producto != null) {
                productoPredicate = cb.equal(root.get("producto"), producto);
                predicateList.add(productoPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            return (ProductoEnVenta) em.createQuery(cq).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public void corregirProductoEnVenta(PuntoVenta puntoDeVenta, String codigoProducto, Double cantidad) throws CodigoProductoNoRegistradoException, Exception {
        Producto producto = productoJpaController.buscarProductoPorCodigo(codigoProducto);
        ProductoEnVenta productoEnVenta = buscarProductoEnVenta(puntoDeVenta, producto);
        if(productoEnVenta != null) {
            String msg = "El punto de venta tiene una cantidad de " + productoEnVenta.getCantidad() + " del producto " + codigoProducto.toUpperCase() + "."
                    + "\n¿Desea reemplazar por la cantidad " + cantidad + "?";
            int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Corregir Inventario", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(showOptionDialog == 0) {
                productoEnVenta.setCantidad(cantidad);
                editarProductoEnVenta(productoEnVenta);
            }
        } else {
            throw new Exception("El punto de venta no contiene este producto.");
        }
    }

    public Boolean productoDisponible(PuntoVenta puntoDeVenta, Producto producto, Double cantidad) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProductoEnVenta> cq = cb.createQuery(ProductoEnVenta.class);
            Root<ProductoEnVenta> root = cq.from(ProductoEnVenta.class);
            cq.select(root);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate puntoDeVentaPredicate, productoPredicate;

            if (puntoDeVenta != null) {
                puntoDeVentaPredicate = cb.equal(root.get("puntoDeVenta"), puntoDeVenta);
                predicateList.add(puntoDeVentaPredicate);
            }

            if (producto != null) {
                productoPredicate = cb.equal(root.get("producto"), producto);
                predicateList.add(productoPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            ProductoEnVenta productoEnVenta = (ProductoEnVenta) em.createQuery(cq).getSingleResult();
            if(productoEnVenta.getCantidad() >= cantidad)
                return true;
            else
                return false;
        } catch(NoResultException ex) {
            throw new Exception("El punto de venta no contiene este producto.");
        } finally {
            em.close();
        }
    }

    public Double cantidadDeProductosEnPuntoDeVenta(PuntoVenta puntoDeVenta, Producto producto) throws Exception {
        ProductoEnVenta productoEnVenta = buscarProductoEnVenta(puntoDeVenta, producto);
        if(productoEnVenta != null) {
            return productoEnVenta.getCantidad();
        } else {
            throw new Exception("El punto de venta no contiene este producto.");
        }
    }

}
