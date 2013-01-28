package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.PrecioAnterior;
import comercio.modelo.Producto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductoJpaController extends Observable implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearProducto(Producto producto) throws Exception {
        EntityManager em = null;
        codigoProductoDisponible(producto.getCodigo());
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(producto);

            PrecioAnterior nuevoPrecio = new PrecioAnterior();
            nuevoPrecio.setValor(producto.getPrecioActual());
            nuevoPrecio.setFecha(new Date(new Date().getTime()));
            nuevoPrecio.setProducto(producto);
            crearPrecioAnterior(nuevoPrecio);

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    public void crearPrecioAnterior(PrecioAnterior precioAnterior) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(precioAnterior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarProducto(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            producto = em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = producto.getId();
                if (encontrarProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    public void editarPrecioAnterior(PrecioAnterior precioAnterior) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            precioAnterior = em.merge(precioAnterior);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = precioAnterior.getId();
                if (encontrarPrecioAnterior(id) == null) {
                    throw new NonexistentEntityException("The precioAnterior with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirProducto(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    public void destruirPrecioAnterior(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PrecioAnterior precioAnterior;
            try {
                precioAnterior = em.getReference(PrecioAnterior.class, id);
                precioAnterior.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The precioAnterior with id " + id + " no longer exists.", enfe);
            }
            em.remove(precioAnterior);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> encontrarProductoEntities() {
        return encontrarProductoEntities(true, -1, -1);
    }

    public List<PrecioAnterior> encontrarPrecioAnteriorEntities() {
        return encontrarPrecioAnteriorEntities(true, -1, -1);
    }

    public List<Producto> encontrarProductoEntities(int maxResults, int firstResult) {
        return encontrarProductoEntities(false, maxResults, firstResult);
    }

    public List<PrecioAnterior> encontrarPrecioAnteriorEntities(int maxResults, int firstResult) {
        return encontrarPrecioAnteriorEntities(false, maxResults, firstResult);
    }

    private List<Producto> encontrarProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    private List<PrecioAnterior> encontrarPrecioAnteriorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PrecioAnterior.class));
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

    public Producto encontrarProducto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public PrecioAnterior encontrarPrecioAnterior(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrecioAnterior.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getPrecioAnteriorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PrecioAnterior> rt = cq.from(PrecioAnterior.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Producto buscarProductoPorCodigo(String codigo) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> p = cq.from(Producto.class);
            cq.select(p).where(cb.equal(p.get("codigo"), codigo.toUpperCase()));
            Query q = em.createQuery(cq);
            return (Producto) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new Exception("El código del producto ingresado no está registrado.");
        } finally {
            em.close();
        }
    }

    private Boolean codigoProductoDisponible(String codigo) throws Exception {
        try {
            buscarProductoPorCodigo(codigo);
            throw new Exception("El código del producto ingresado ya está registrado.");
        } catch (Exception ex) {
            return true;
        }
    }

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> productos = new ArrayList();
        Object[] array = encontrarProductoEntities().toArray();
        for(Object o : array)
            productos.add((Producto) o);
        return productos;
    }

    public Double obtenerDescuentoVigente(Producto producto) {
        return new Double("1");
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

}
