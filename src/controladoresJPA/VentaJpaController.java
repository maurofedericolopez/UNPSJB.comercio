package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.CodigoProductoNoRegistradoException;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 * Ésta clase se encarga de las operaciones CRUD de las entidades <code>MedioDePago</code>, <code>Venta</code> e <code>ItemVenta</code>.
 * También es responsable de las transacciones de venta.
 * @author Mauro Federico Lopez
 */
public class VentaJpaController extends Observable implements Serializable {

    private EntityManagerFactory emf = null;
    private PuntoVentaJpaController puntoDeVentaJpaController;
    private ProductoJpaController productoJpaController;
    private PuntoVenta puntoDeVenta = null;
    private MedioDePago medioDePago = null;
    private ArrayList<ItemVenta> itemsDeVenta = new ArrayList();

    /**
     * Construye un nuevo controlador para las entidades <code>MedioDePago</code>, <code>Venta</code> e <code>ItemVenta</code>.
     */
    public VentaJpaController() {
        this.emf = ControllerSingleton.getEmf();
        puntoDeVentaJpaController = ControllerSingleton.getPuntoVentaJpaController();
        productoJpaController = ControllerSingleton.getProductoJpaController();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Venta</code> en la base de datos.
     * @param venta es la <code>Venta</code> que se persistirá.
     */
    public void crearVenta(Venta venta) {
        if (venta.getItems() == null) {
            venta.setItems(new ArrayList<ItemVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ItemVenta> attachedItems = new ArrayList();
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

    /**
     * Persiste un objeto <code>ItemVenta</code> en la base de datos.
     * @param itemVenta es el <code>ItemVenta</code> que se persistirá.
     */
    public void crearItemVenta(ItemVenta itemVenta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta aVenta = itemVenta.getVenta();
            if (aVenta != null) {
                aVenta = em.getReference(aVenta.getClass(), aVenta.getId());
                itemVenta.setVenta(aVenta);
            }
            em.persist(itemVenta);
            if (aVenta != null) {
                aVenta.getItems().add(itemVenta);
                aVenta = em.merge(aVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Persiste un objeto <code>MedioDePago</code> en la base de datos.
     * @param medioPago es el <code>MedioDePago</code> que se persistirá.
     */
    public void crearMedioDePago(MedioDePago medioPago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medioPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza un objeto <code>Venta</code> en la base de datos.
     * @param venta es la <code>Venta</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la venta que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarVenta(Venta venta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getId());
            List<ItemVenta> itemsOld = persistentVenta.getItems();
            List<ItemVenta> itemsNew = venta.getItems();
            List<ItemVenta> attachedItemsNew = new ArrayList();
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

    /**
     * Actualiza un objeto <code>ItemVenta</code> en la base de datos.
     * @param itemVenta es el <code>ItemVenta</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el item de venta que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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

    /**
     * Actualiza un objeto <code>MedioDePago</code> en la base de datos.
     * @param medioPago es el <code>MedioDePago</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el medio de pago que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarMedioDePago(MedioDePago medioPago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            medioPago = em.merge(medioPago);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = medioPago.getId();
                if (encontrarMedioPago(id) == null) {
                    throw new NonexistentEntityException("The medioPago with id " + id + " no longer exists.");
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
     * Elimina una venta de la base de datos con el <code>id</code> especificado.
     * @param id el id de la venta en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la venta que se quiere eliminar no existe en la base de datos.
     */
    public void destruirVenta(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta aVenta;
            try {
                aVenta = em.getReference(Venta.class, id);
                aVenta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<ItemVenta> items = aVenta.getItems();
            for (ItemVenta itemsItemVenta : items) {
                itemsItemVenta.setVenta(null);
                itemsItemVenta = em.merge(itemsItemVenta);
            }
            em.remove(aVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina un item de venta de la base de datos con el <code>id</code> especificado.
     * @param id el id del item de venta en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el item de venta que se quiere eliminar no existe en la base de datos.
     */
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
            Venta aVenta = itemVenta.getVenta();
            if (aVenta != null) {
                aVenta.getItems().remove(itemVenta);
                aVenta = em.merge(aVenta);
            }
            em.remove(itemVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Elimina un medio de pago de la base de datos con el <code>id</code> especificado.
     * @param id el id del medio de pago en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el medio de pago que se quiere eliminar no existe en la base de datos.
     */
    public void destruirMedioDePago(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedioDePago medioPago;
            try {
                medioPago = em.getReference(MedioDePago.class, id);
                medioPago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medioPago with id " + id + " no longer exists.", enfe);
            }
            em.remove(medioPago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Venta> encontrarVentaEntities() {
        return encontrarVentaEntities(true, -1, -1);
    }

    private List<ItemVenta> encontrarItemVentaEntities() {
        return encontrarItemVentaEntities(true, -1, -1);
    }

    private List<MedioDePago> encontrarMedioPagoEntities() {
        return encontrarMedioPagoEntities(true, -1, -1);
    }

    private List<Venta> encontrarVentaEntities(int maxResults, int firstResult) {
        return encontrarVentaEntities(false, maxResults, firstResult);
    }

    private List<ItemVenta> encontrarItemVentaEntities(int maxResults, int firstResult) {
        return encontrarItemVentaEntities(false, maxResults, firstResult);
    }

    private List<MedioDePago> encontrarMedioPagoEntities(int maxResults, int firstResult) {
        return encontrarMedioPagoEntities(false, maxResults, firstResult);
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

    private List<MedioDePago> encontrarMedioPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MedioDePago.class));
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
     * Devuelve un objeto <code>Venta</code> buscado por su id en la base de datos.
     * @param id el id de la venta en la base de datos.
     * @return venta
     */
    public Venta encontrarVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>ItemVenta</code> buscado por su id en la base de datos.
     * @param id el id del item de venta en la base de datos.
     * @return itemVenta
     */
    public ItemVenta encontrarItemVenta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemVenta.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>MedioDePago</code> buscado por su id en la base de datos.
     * @param id el id del medio de pago en la base de datos.
     * @return medioDePago
     */
    public MedioDePago encontrarMedioPago(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedioDePago.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de ventas registradas en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de items de venta registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de medios de pago registrados en la base de datos.
     * @return cantidad
     */
    public int getMedioPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MedioDePago> rt = cq.from(MedioDePago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * 
     * @param index
     * @throws Exception 
     */
    public void eliminarItemDeVenta(int index) throws Exception {
        if(index >= 0) {
            ItemVenta itemDeVenta = itemsDeVenta.remove(index);
            Producto producto = itemDeVenta.getProducto();
            Double cantidad = itemDeVenta.getCantidad();
            puntoDeVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);
            notificarCambios();
        } else {
            throw new Exception("No ha seleccionado ningún item.");
        }
    }

    /**
     * Devuelve una lista de todos los medios de pago registrados en la base de datos.
     * Devuelve un ArrayList de medios de pago.
     * @return mediosDePago
     */
    public ArrayList<MedioDePago> obtenerTodosLosMediosDePago() {
        ArrayList<MedioDePago> mediosDePago = new ArrayList();
        Object[] array = encontrarMedioPagoEntities().toArray();
        for(Object o : array)
            mediosDePago.add((MedioDePago) o);
        return mediosDePago;
    }

    /**
     * Cancela toda la transaccion y vuelve los stock a su estado anterior.
     * @throws Exception 
     */
    public void cancelarVenta() throws Exception {
        Iterator<ItemVenta> i = itemsDeVenta.iterator();
        while(i.hasNext()) {
            ItemVenta itemDeVenta = i.next();
            Producto producto = itemDeVenta.getProducto();
            Double cantidad = itemDeVenta.getCantidad();
            puntoDeVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);
        }
        itemsDeVenta.clear();
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

    /**
     * Agrega un objecto <code>ItemVenta</code> con el codigoProducto y la cantidadProducto especificada.
     * @param codigoProducto el código del producto para el item.
     * @param cantidadProducto es la cantidad de productos para el item.
     * @throws CodigoProductoNoRegistradoException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     * @throws Exception 
     */
    public void agregarItemDeVenta(String codigoProducto, Double cantidadProducto) throws CodigoProductoNoRegistradoException, Exception {
        Producto producto = productoJpaController.buscarProductoPorCodigo(codigoProducto);
        Boolean productoDisponible = puntoDeVentaJpaController.productoDisponible(puntoDeVenta, producto, cantidadProducto);
        if(productoDisponible) {
            puntoDeVentaJpaController.descontarDePuntoDeVenta(puntoDeVenta, producto, cantidadProducto);
            if (!productoAgregado(producto)) {
                ItemVenta itemDeVenta = new ItemVenta();
                itemDeVenta.setProducto(producto);
                itemDeVenta.setPrecio(producto.getPrecioActual());
                itemDeVenta.setDescuento(productoJpaController.obtenerDescuentoVigente(producto));
                itemDeVenta.setCantidad(cantidadProducto);
                itemsDeVenta.add(itemDeVenta);
            } else {
                ItemVenta obtenerItemDeVenta = obtenerItemDeVenta(producto);
                Double nuevaCantidad = obtenerItemDeVenta.getCantidad() + cantidadProducto;
                obtenerItemDeVenta.setCantidad(nuevaCantidad);
            }
            notificarCambios();
        } else {
            throw new Exception("No hay cantidad suficiente del producto " + producto.getDescripcion() + " para satisfacer la venta");
        }
    }

    /**
     * @param puntoDeVenta the puntoDeVenta to set
     */
    public void setPuntoDeVenta(PuntoVenta puntoDeVenta) {
        this.puntoDeVenta = puntoDeVenta;
        itemsDeVenta = new ArrayList();
        notificarCambios();
    }

    /**
     * Devuelve el monto total de la venta en curso.
     * El monto es la suma del producto entre el precio, el descuento y la cantidad de cada producto.
     * @return montoTotal
     */
    public Double obtenerMontoTotal() {
        Double montoTotal = 0.0;
        Iterator<ItemVenta> i = itemsDeVenta.iterator();
        while(i.hasNext()) {
            ItemVenta itemDeVenta = i.next();
            montoTotal += itemDeVenta.getPrecio() * itemDeVenta.getDescuento() * itemDeVenta.getCantidad();
        }
        return montoTotal;
    }

    /**
     * @param medioDePago the medioDePago to set
     */
    public void setMedioDePago(MedioDePago medioDePago) {
        this.medioDePago = medioDePago;
    }

    /**
     * Persiste los datos de la venta y los items de venta.
     * @throws Exception 
     */
    public void persistirOperacion() throws Exception {
        Boolean b = (medioDePago != null) && (!itemsDeVenta.isEmpty()) && (puntoDeVenta != null);
        if(b) {
            Long codigo = generarCodigoVenta();
            Venta venta = new Venta();
            venta.setFecha(new Date());
            venta.setCodigo(codigo);
            venta.setMedioDePago(medioDePago);
            venta.setPuntoVenta(puntoDeVenta);
            crearVenta(venta);
            Iterator<ItemVenta> i = itemsDeVenta.iterator();
            while(i.hasNext()) {
                ItemVenta itemDeVenta = i.next();
                itemDeVenta.setVenta(venta);
                crearItemVenta(itemDeVenta);
            }
            venta.setItems(itemsDeVenta);
            editarVenta(venta);
            itemsDeVenta.clear();
            notificarCambios();
        } else {
            throw new Exception("Hay campos incompletos");
        }
    }

    private Long generarCodigoVenta() {
        Long codigo = UUID.randomUUID().getMostSignificantBits();
        while(!codigoVentaDisponible(codigo)) {
            codigo = UUID.randomUUID().getMostSignificantBits();
        }
        return codigo;
    }

    /**
     * Devolverá true si el codigo de la venta no fue registrado en la base de datos.
     * @param codigo es el codigo de la venta que se analizará.
     * @return boolean
     */
    public Boolean codigoVentaDisponible(Long codigo) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Venta> c = cb.createQuery(Venta.class);
            Root<Venta> p = c.from(Venta.class);
            c.select(p).where(cb.equal(p.get("codigo"), codigo));
            Venta venta = em.createQuery(c).getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        } finally {
            em.close();
        }
    }

    /**
     * 
     * @param producto
     * @return 
     */
    private Boolean productoAgregado(Producto producto) {
        Iterator<ItemVenta> i = itemsDeVenta.iterator();
        while(i.hasNext()) {
            if(i.next().getProducto().equals(producto)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Devolve un objeto <code>ItemVenta</code> de la lista de items de la venta en curso con el producto especificado.
     * @param producto es el <code>Producto</code> por el cual se buscará el item de venta.
     * @return itemDeVenta
     */
    private ItemVenta obtenerItemDeVenta(Producto producto) {
        Iterator<ItemVenta> i = itemsDeVenta.iterator();
        while(i.hasNext()) {
            ItemVenta itemDeVenta = i.next();
            if(itemDeVenta.getProducto().equals(producto)) {
                return itemDeVenta;
            }
        }
        return null;
    }

    /**
     * Devuelve una lista con todos los items de venta que tenga un producto de una marca especificada.
     * @param marca es la <code>Marca</code> por la cual se filtraran los items de venta.
     * @return ventasPorMarca
     */
    public ArrayList<ItemVenta> obtenerVentasPorMarca(Marca marca) {
        ArrayList<ItemVenta> ventasPorMarca = new ArrayList();
        Iterator<Producto> i = productoJpaController.obtenerProductosPorMarca(marca).iterator();
        while(i.hasNext()) {
            Producto producto = i.next();
            Double cantidad = 0.0;
            Iterator<ItemVenta> j = obtenerItemsDeVentaPorProducto(producto).iterator();
            while(j.hasNext()) {
                cantidad += j.next().getCantidad();
            }
            ItemVenta itemDeVenta = new ItemVenta();
            itemDeVenta.setProducto(producto);
            itemDeVenta.setCantidad(cantidad);
            itemDeVenta.setPrecio(0.0);
            itemDeVenta.setDescuento(0.0);
            itemDeVenta.setVenta(null);
            ventasPorMarca.add(itemDeVenta);
        }
        return ventasPorMarca;
    }

    /**
     * Devuelve una lista con todos los items de venta que tenga un producto de una categoría especificada.
     * @param categoria es la <code>Categoria</code> por la cual se filtraran los items de venta.
     * @return ventasPorCategoria
     */
    public ArrayList<ItemVenta> obtenerVentasPorCategoria(Categoria categoria) {
        ArrayList<ItemVenta> ventasPorCategoria = new ArrayList();
        Iterator<Producto> i = productoJpaController.obtenerProductosPorCategoria(categoria).iterator();
        while(i.hasNext()) {
            Producto producto = i.next();
            Double cantidad = 0.0;
            Iterator<ItemVenta> j = obtenerItemsDeVentaPorProducto(producto).iterator();
            while(j.hasNext()) {
                cantidad += j.next().getCantidad();
            }
            ItemVenta itemDeVenta = new ItemVenta();
            itemDeVenta.setProducto(producto);
            itemDeVenta.setCantidad(cantidad);
            itemDeVenta.setPrecio(0.0);
            itemDeVenta.setDescuento(0.0);
            itemDeVenta.setVenta(null);
            ventasPorCategoria.add(itemDeVenta);
        }
        return ventasPorCategoria;
    }

    /**
     * Devuelve una lista de items de venta que tiene al producto especificado.
     * @param producto es la <code>Producto</code> por la cual se filtraran los items de venta.
     * @return itemsDeVenta
     */
    private ArrayList<ItemVenta> obtenerItemsDeVentaPorProducto(Producto producto) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ItemVenta> cq = cb.createQuery(ItemVenta.class);
            Root<ItemVenta> root = cq.from(ItemVenta.class);
            cq.select(root);
            cq.where(cb.equal(root.get("producto"), producto));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<ItemVenta> losItemsDeVenta = new ArrayList();
            for(Object o : array) {
                losItemsDeVenta.add((ItemVenta) o);
            }
            return losItemsDeVenta;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

}
