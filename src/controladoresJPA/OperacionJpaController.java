package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class OperacionJpaController implements Serializable {

    public OperacionJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
    }

    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private void crearOperacion(Operacion operacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(operacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void editarOperacion(Operacion operacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            operacion = em.merge(operacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = operacion.getId();
                if (encontrarOperacion(id) == null) {
                    throw new NonexistentEntityException("La operación que desea actualizar en la base de datos no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void destruirOperacion(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Operacion operacion;
            try {
                operacion = em.getReference(Operacion.class, id);
                operacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La operación que desea eliminar de la base de datos no existe.", enfe);
            }
            em.remove(operacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Operacion encontrarOperacion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Operacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Operacion> rt = cq.from(Operacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void registrarOperacionVenta(Empleado empleado, Venta venta) {
        Iterator<ItemVenta> i = venta.getItems().iterator();
        while(i.hasNext()) {
            ItemVenta next = i.next();
            Operacion operacion = new Operacion();
            operacion.setEmpleado(empleado);
            operacion.setFecha(new Date());
            operacion.setDescripcion("Vendió " + next.getCantidad().toString() + " " + next.getProducto().getUnidad().toString() + " del producto " + next.getProducto().getCodigo().toString() + ".");
            crearOperacion(operacion);
        }
    }

    public void registrarOperacionEgreso(Empleado empleado, Egreso egreso) {
        Iterator<LoteEgresado> i = egreso.getLotesEgresados().iterator();
        while(i.hasNext()) {
            LoteEgresado next = i.next();
            Operacion operacion = new Operacion();
            operacion.setEmpleado(empleado);
            operacion.setFecha(new Date());
            operacion.setDescripcion("Egresó " + next.getCantidad().toString() + " " + next.getLote().getProducto().getUnidad().toString() + " del lote " + next.getLote().getCodigo() + " del " + next.getEgreso().getAlmacen().toString() + ".");
            crearOperacion(operacion);
        }
    }

    public void registrarOperacionIngreso(Empleado empleado, Remito remito) {
        Iterator<LoteRemito> i = remito.getLotesDelRemito().iterator();
        while(i.hasNext()) {
            LoteRemito next = i.next();
            Operacion operacion = new Operacion();
            operacion.setEmpleado(empleado);
            operacion.setFecha(new Date());
            operacion.setDescripcion("Ingresó el lote " + next.getLote().getCodigo() + " con " + next.getCantidadIngresada().toString() + " " + next.getLote().getProducto().getUnidad().toString() + ".");
            crearOperacion(operacion);
        }
    }

    public void registrarOperacionTransferenciaDeLotesDeProductos(Empleado empleado, Almacen almacenOrigen, PuntoVenta puntoDeVentaDestino, Lote lote, Double cantidad) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Transfirió " + cantidad + " " + lote.getProducto().getUnidad().toString() + " del lote " + lote.getCodigo() + ", del " + almacenOrigen.toString() + " al " + puntoDeVentaDestino.toString() + ".");
        crearOperacion(operacion);
    }

    public void registrarOperacionTransferenciaDeLotesDeProductos(Empleado empleado, Almacen almacenOrigen, Almacen almacenDestino, Lote lote, Double cantidad) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Transfirió " + cantidad + " " + lote.getProducto().getUnidad().toString() + " del lote " + lote.getCodigo() + ", del " + almacenOrigen.toString() + " al " + almacenDestino.toString() + ".");
        crearOperacion(operacion);
    }

    public void registrarOperacionModificacionDePrecioDeUnProducto(Empleado empleado, Producto producto) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Modificó el precio del producto " + producto.getCodigo() + " a " + producto.getPrecioActual() + ".");
        crearOperacion(operacion);
    }

    public void registrarOperacionCreacionDeOfertaParaUnProducto(Empleado empleado, Producto producto) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Creó una oferta para el producto " + producto.getCodigo() + ".");
        crearOperacion(operacion);
    }

    public void registrarOperacionCorreccionDeInventario(Empleado empleado, ProductoEnVenta productoEnVenta) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Corrigió el producto " + productoEnVenta.getProducto().getCodigo() + "");
        crearOperacion(operacion);
    }

    public void registrarOperacionCorreccionDeInventario(Empleado empleado, LoteAlmacenado loteAlmacenado) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Corrigió el lote " + loteAlmacenado.getLote().getCodigo() + " almacenado en " + loteAlmacenado.getAlmacen().toString() + " a " + loteAlmacenado.getCantidad());
        crearOperacion(operacion);
    }

    public void registrarOperacionCreacionDeProducto(Empleado empleado, Producto producto) {
        Operacion operacion = new Operacion();
        operacion.setEmpleado(empleado);
        operacion.setFecha(new Date());
        operacion.setDescripcion("Registró el producto " + producto.getCodigo() + " con el precio $" + producto.getPrecioActual());
        crearOperacion(operacion);
    }

    public ArrayList<Operacion> obtenerOperacionesMovimientoInventario() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operacion.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Operacion> operaciones = new ArrayList();
            for(Object o : array)
                operaciones.add((Operacion) o);
            return operaciones;
        } catch (NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<Operacion> obtenerOperacionesPorFecha(Date fecha) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Operacion> c = cb.createQuery(Operacion.class);
            Root<Operacion> p = c.from(Operacion.class);
            c.select(p).where(cb.equal(p.get("fecha"), fecha));
            ArrayList<Operacion> operaciones = new ArrayList();
            Object[] array = em.createQuery(c).getResultList().toArray();
            for(Object o : array)
                operaciones.add((Operacion) o);
            return operaciones;
        } catch (NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<Operacion> obtenerOperacionesPorEmpleado(Empleado empleado) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Operacion> c = cb.createQuery(Operacion.class);
            Root<Operacion> p = c.from(Operacion.class);
            c.select(p).where(cb.equal(p.get("empleado"), empleado));
            ArrayList<Operacion> operaciones = new ArrayList();
            Object[] array = em.createQuery(c).getResultList().toArray();
            for(Object o : array)
                operaciones.add((Operacion) o);
            return operaciones;
        } catch (NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

}
