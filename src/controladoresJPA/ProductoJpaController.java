package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.CodigoProductoNoDisponibleException;
import controladoresJPA.exceptions.CodigoProductoNoRegistradoException;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;
import modelo.*;

/**
 * Ésta clase se encarga de las operaciones CRUD de laa entidades <code>Producto</code>, <code>PrecioAnterior</code> y <code>Oferta</code>.
 * También es responsable de modificar precios y asociar ofertas a los productos.
 * @author Mauro Federico Lopez
 */
public class ProductoJpaController extends Observable implements Serializable {

    /**
     * Construye un nuevo controlador para las entidades <code>Producto</code>, <code>PrecioAnterior</code> y <code>Oferta</code>.
     */
    public ProductoJpaController() {
        this.emf = ControllerSingleton.getEmf();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Producto</code> en la base de datos.
     * @param producto es el <code>Producto</code> que se persistirá.
     */
    public void crearProducto(Producto producto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    /**
     * Persiste un objeto <code>PrecioAnterior</code> en la base de datos.
     * @param precioAnterior es el <code>PrecioAnterior</code> que se persistirá.
     */
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
            notificarCambios();
        }
    }

    /**
     * Persiste un objeto <code>Oferta</code> en la base de datos.
     * @param oferta es la <code>Oferta</code> que se persistirá.
     */
    public void crearOferta(Oferta oferta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(oferta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza un objeto <code>Producto</code> en la base de datos.
     * @param producto es el <code>Producto</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el producto que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarProducto(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            producto = em.merge(producto);
            em.getTransaction().commit();
            notificarCambios();
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
        }
    }

    /**
     * Actualiza un objeto <code>PrecioAnterior</code> en la base de datos.
     * @param precioAnterior es el <code>PrecioAnterior</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el precio anterior que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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
            notificarCambios();
        }
    }

    /**
     * Actualiza un objeto <code>Oferta</code> en la base de datos.
     * @param oferta es la <code>Oferta</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la oferta que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarOferta(Oferta oferta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            oferta = em.merge(oferta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = oferta.getId();
                if (encontrarOferta(id) == null) {
                    throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.");
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

    /**
     * Elimina un producto de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del producto en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el producto que se quiere eliminar no existe en la base de datos.
     */
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

    /**
     * Elimina un precio anterior de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del precio anterior en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el precio anterior que se quiere eliminar no existe en la base de datos.
     */
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
            notificarCambios();
        }
    }

    /**
     * Elimina una oferta de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> de la oferta en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la oferta que se quiere eliminar no existe en la base de datos.
     */
    public void destruirOferta(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oferta oferta;
            try {
                oferta = em.getReference(Oferta.class, id);
                oferta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.", enfe);
            }
            em.remove(oferta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Producto> encontrarProductoEntities() {
        return encontrarProductoEntities(true, -1, -1);
    }

    private List<PrecioAnterior> encontrarPrecioAnteriorEntities() {
        return encontrarPrecioAnteriorEntities(true, -1, -1);
    }

    private List<Oferta> encontrarOfertaEntities() {
        return encontrarOfertaEntities(true, -1, -1);
    }

    private List<Producto> encontrarProductoEntities(int maxResults, int firstResult) {
        return encontrarProductoEntities(false, maxResults, firstResult);
    }

    private List<PrecioAnterior> encontrarPrecioAnteriorEntities(int maxResults, int firstResult) {
        return encontrarPrecioAnteriorEntities(false, maxResults, firstResult);
    }

    private List<Oferta> encontrarOfertaEntities(int maxResults, int firstResult) {
        return encontrarOfertaEntities(false, maxResults, firstResult);
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

    private List<Oferta> encontrarOfertaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oferta.class));
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

    /**
     * Devuelve un objeto <code>Producto</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del producto en la base de datos.
     * @return producto
     */
    public Producto encontrarProducto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>PrecioAnterior</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del precio anterior en la base de datos.
     * @return precioAnterior
     */
    public PrecioAnterior encontrarPrecioAnterior(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PrecioAnterior.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>Oferta</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> de la oferta en la base de datos.
     * @return oferta
     */
    public Oferta encontrarOferta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oferta.class, id);
        } finally {
            em.close();
        }
    }

    
    /**
     * Devuelve la cantidad de productos registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de precios anteriores registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de ofertas registrados en la base de datos.
     * @return cantidad
     */
    public int getOfertaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oferta> rt = cq.from(Oferta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * 
     * @param codigo
     * @return producto
     * @throws CodigoProductoNoRegistradoException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     */
    public Producto buscarProductoPorCodigo(String codigo) throws CodigoProductoNoRegistradoException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> p = cq.from(Producto.class);
            cq.select(p).where(cb.equal(p.get("codigo"), codigo.toUpperCase()));
            Query q = em.createQuery(cq);
            return (Producto) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new CodigoProductoNoRegistradoException();
        } finally {
            em.close();
        }
    }

    /**
     * Devolverá <code>true</code> si el codigo del producto no fue registrado en la base de datos.
     * @param codigo es el codigo que se analizará.
     * @return boolean
     * @throws CodigoProductoNoDisponibleException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     */
    public Boolean codigoProductoDisponible(String codigo) throws CodigoProductoNoDisponibleException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> p = cq.from(Producto.class);
            cq.select(p).where(cb.equal(p.get("codigo"), codigo.toUpperCase()));
            Producto singleResult = em.createQuery(cq).getSingleResult();
            throw new CodigoProductoNoDisponibleException();
        } catch (NoResultException ex) {
            return true;
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista de todos los productos registrados en la base de datos.
     * Devuelve un ArrayList de productos.
     * @return productos
     */
    public ArrayList<Producto> obtenerTodosLosProductos() {
        ArrayList<Producto> productos = new ArrayList();
        Object[] array = encontrarProductoEntities().toArray();
        for(Object o : array)
            productos.add((Producto) o);
        return productos;
    }

    /**
     * Devuelve el descuento vigente del producto.
     * Si no tiene ninguna oferta asociada o si la oferta asociada que tiene ha caducado devuelve 0.
     * @param producto es el <code>Producto</code> de cual se extraerá el descuento.
     * @return descuento
     */
    public Double mostrarDescuentoVigente(Producto producto) {
        Oferta oferta = producto.getOferta();
        Date hoy = new Date();
        if(oferta != null && (oferta.getFechaInicio().before(hoy) && oferta.getFechaFin().after(hoy)))
            return oferta.getDescuento();
        else
            return 0.0;
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    /**
     * Devuelve una lista con todos los productos de una categoría.
     * @param categoria es la <code>Categoria</code> por la cual se realizará el filtro de produtos.
     * @return productosPorCategoria
     */
    public ArrayList<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> root = cq.from(Producto.class);
            cq.select(root);

            Predicate categoriaPredicate = null;

            if (categoria != null) {
                categoriaPredicate = cb.equal(root.get("categoria"), categoria);
            }
            cq.where(categoriaPredicate);
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Producto> productosPorCategoria = new ArrayList();
            for(Object o : array)
                productosPorCategoria.add((Producto) o);
            return productosPorCategoria;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista con todos los productos de una marca.
     * @param marca es la <code>Marca</code> por la cual se realizará el filtro de produtos.
     * @return productosPorMarca
     */
    public ArrayList<Producto> obtenerProductosPorMarca(Marca marca) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> root = cq.from(Producto.class);
            cq.select(root);

            Predicate marcaPredicate = null;

            if (marca != null) {
                marcaPredicate = cb.equal(root.get("marca"), marca);
            }
            cq.where(marcaPredicate);
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Producto> productosPorMarca = new ArrayList();
            for(Object o : array)
                productosPorMarca.add((Producto) o);
            return productosPorMarca;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    /**
     * Modifica el precio un producto por porcentaje.
     * @param codigoProducto es el codigo del producto al cual se le modificará el precio por porcentaje.
     * @param porcentaje es el porcentaje que se le aplicará al actual precio del producto, para luego reemplazarlo.
     * @throws CodigoProductoNoRegistradoException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     * @throws Exception 
     */
    public void modificarPrecioProducto(String codigoProducto, Double porcentaje) throws CodigoProductoNoRegistradoException, Exception {
        Producto producto = buscarProductoPorCodigo(codigoProducto);
        if(porcentaje < 0)
            porcentaje = 0.0;
        modificarPrecioProducto(producto, porcentaje);
    }

    /**
     * Modifica el precio un producto por porcentaje.
     * @param producto es el <code>Producto</code> al cual se le modificará el precio por porcentaje.
     * @param porcentaje es el porcentaje que se le aplicará al actual precio del producto, para luego reemplazarlo.
     * @throws Exception 
     */
    public void modificarPrecioProducto(Producto producto, Double porcentaje) throws Exception {
        Double nuevoPrecio = producto.getPrecioActual() * porcentaje;
        PrecioAnterior precioAnterior = new PrecioAnterior();
        precioAnterior.setFecha(new Date());
        precioAnterior.setProducto(producto);
        precioAnterior.setValor(nuevoPrecio);
        crearPrecioAnterior(precioAnterior);
        producto.setPrecioActual(nuevoPrecio);
        editarProducto(producto);
    }

    /**
     * Modifica el precio de los productos de una categoría.
     * @param categoria es la <code>Categoria</code> por la cual se filtrarán los productos.
     * @param porcentaje es el porcentaje que se le aplicará al actual precio del producto, para luego reemplazarlo.
     * @throws Exception 
     */
    public void modificarPrecioCategoria(Categoria categoria, Double porcentaje) throws Exception {
        if(porcentaje < 0)
            porcentaje = 0.0;
        ArrayList<Producto> productos = obtenerProductosPorCategoria(categoria);
        Iterator<Producto> i = productos.iterator();
        while(i.hasNext()) {
            modificarPrecioProducto(i.next(), porcentaje);
        }
    }

    /**
     * Modifica el precio de los productos de una marca.
     * @param marca es la <code>Marca</code> por la cual se filtrarán los productos.
     * @param porcentaje es el porcentaje que se le aplicará al actual precio del producto, para luego reemplazarlo.
     * @throws Exception 
     */
    public void modificarPrecioMarca(Marca marca, Double porcentaje) throws Exception {
        if(porcentaje < 0)
            porcentaje = 0.0;
        ArrayList<Producto> productos = obtenerProductosPorMarca(marca);
        Iterator<Producto> i = productos.iterator();
        while(i.hasNext()) {
            modificarPrecioProducto(i.next(), porcentaje);
        }
    }

    /**
     * Devuelve el descuento que se aplicará al producto especificado.
     * @param producto es el <code>Producto</code> del cual se extraerá el descuento.
     * @return descuento
     */
    public Double obtenerDescuentoVigente(Producto producto) {
        return (1 - mostrarDescuentoVigente(producto));
    }

    /**
     * Agrega un producto al catálogo de productos.
     * @param producto es el <code>Producto</code> que se agregará a la base de datos.
     * @throws CodigoProductoNoDisponibleException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     */
    public void registrarProducto(Producto producto) throws CodigoProductoNoDisponibleException {
        codigoProductoDisponible(producto.getCodigo());
        

        PrecioAnterior nuevoPrecio = new PrecioAnterior();
        nuevoPrecio.setValor(producto.getPrecioActual());
        nuevoPrecio.setFecha(new Date(new Date().getTime()));
        nuevoPrecio.setProducto(producto);

        crearPrecioAnterior(nuevoPrecio);
    }

    /**
     * Crea una oferta y la asocia al producto especificado.
     * @param codigoProducto es el codigo del producto al cual se le asociará la oferta.
     * @param descuento es el descuento de la oferta.
     * @param fechaInicio es la fecha de inicio de la oferta.
     * @param fechaFin es la fecha de finalización de la oferta.
     * @throws Exception 
     */
    public void crearOfertaParaProducto(String codigoProducto, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        Producto producto = buscarProductoPorCodigo(codigoProducto);
        crearOfertaParaProducto(producto, descuento, fechaInicio, fechaFin);
    }

    /**
     * Crea una oferta y la asocia al producto especificado.
     * @param producto es el <code>Producto</code> al cual se le asociará la oferta.
     * @param descuento es el descuento de la oferta.
     * @param fechaInicio es la fecha de inicio de la oferta.
     * @param fechaFin es la fecha de finalización de la oferta.
     * @throws Exception 
     */
    public void crearOfertaParaProducto(Producto producto, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        if(descuento < 0.0) {
            descuento = 0.0;
        } else
            if(descuento > 1) {
                descuento = 1.0;
            }
        Oferta oferta = producto.getOferta();
        if(oferta != null) {
            if (oferta.getFechaFin().after(new Date())) {
                String msg = "El producto " + producto.getDescripcion() + " tiene una oferta asociada en vigencia."
                        + "\nÉsta vence el " + oferta.getFechaFin().toLocaleString()
                        + "\n¿Desea reemplazar la oferta?";
                int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Oferta", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (showOptionDialog == 0) {
                    oferta.setDescuento(descuento);
                    oferta.setFechaFin(fechaFin);
                    oferta.setFechaInicio(fechaInicio);
                    editarOferta(oferta);
                }
            } else {
                oferta.setDescuento(descuento);
                oferta.setFechaFin(fechaFin);
                oferta.setFechaInicio(fechaInicio);
                editarOferta(oferta);
            }
        } else {
            oferta = new Oferta();
            oferta.setDescuento(descuento);
            oferta.setFechaFin(fechaFin);
            oferta.setFechaInicio(fechaInicio);
            crearOferta(oferta);
            producto.setOferta(oferta);
            editarProducto(producto);
        }
    }

    /**
     * Crea una oferta y la asocia a los productos de una categoría.
     * @param categoria es la <code>Categoria</code> a la cual se le asociará la oferta.
     * @param descuento es el descuento de la oferta.
     * @param fechaInicio es la fecha de inicio de la oferta.
     * @param fechaFin es la fecha de finalización de la oferta.
     * @throws Exception 
     */
    public void crearOfertaParaProductosDeUnaCategoria(Categoria categoria, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        Oferta oferta = categoria.getOferta();
        Iterator<Producto> i = obtenerProductosPorCategoria(categoria).iterator();    
        if(oferta == null) {
            while(i.hasNext()) {
                crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
            }
            oferta.setDescuento(descuento);
            oferta.setFechaInicio(fechaInicio);
            oferta.setFechaFin(fechaFin);
            editarOferta(oferta);
        } else {
            String msg = "La categoría tiene una oferta asociadad y vence el " + oferta.getFechaFin().toLocaleString()
                        + "\n¿Desea reemplazar la oferta asociada a la categoría?";
            int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Oferta", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (showOptionDialog == 0) {
                while(i.hasNext()) {
                    crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
                }
                oferta.setDescuento(descuento);
                oferta.setFechaInicio(fechaInicio);
                oferta.setFechaFin(fechaFin);
                editarOferta(oferta);
            }
        }
    }

    /**
     * Crea una oferta y la asocia a los productos de una marca.
     * @param marca es la <code>Marca</code> a la cual se le asociará la oferta.
     * @param descuento es el descuento de la oferta.
     * @param fechaInicio es la fecha de inicio de la oferta.
     * @param fechaFin es la fecha de finalización de la oferta.
     * @throws Exception 
     */
    public void crearOfertaParaProductosDeUnaMarca(Marca marca, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        Oferta oferta = marca.getOferta();
        Iterator<Producto> i = obtenerProductosPorMarca(marca).iterator();    
        if(oferta == null) {
            while(i.hasNext()) {
                crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
            }
            oferta.setDescuento(descuento);
            oferta.setFechaInicio(fechaInicio);
            oferta.setFechaFin(fechaFin);
            editarOferta(oferta);
        } else {
            String msg = "La marca tiene una oferta asociadad y vence el " + oferta.getFechaFin().toLocaleString()
                        + "\n¿Desea reemplazar la oferta asociada a la marca?";
            int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Oferta", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (showOptionDialog == 0) {
                while(i.hasNext()) {
                    crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
                }
                oferta.setDescuento(descuento);
                oferta.setFechaInicio(fechaInicio);
                oferta.setFechaFin(fechaFin);
                editarOferta(oferta);
            }
        }
    }

}
