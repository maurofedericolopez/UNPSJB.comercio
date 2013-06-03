package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.CodigoProductoNoRegistradoException;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 * Ésta clase se encarga de las operaciones CRUD de la entidad <code>Remito</code>.
 * Tambien es responsable de llevar a cabo todos las importaciones de productos a un almacén.
 * @author Mauro Federico Lopez
 */
public class RemitoJpaController extends Observable implements Serializable {

    private EntityManagerFactory emf = null;
    private LoteJpaController loteJpaController;
    private ProductoJpaController productoJpaController;
    private OperacionJpaController operacionJpaController;

    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();
    private Remito remito = null;
    private Almacen almacen = null;

    /**
     * Construye un nuevo controlador para la entidad <code>Remito</code>.
     */
    public RemitoJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
        loteJpaController = ControllerSingleton.getLoteJpaController();
        productoJpaController = ControllerSingleton.getProductoJpaController();
        operacionJpaController = new OperacionJpaController();
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Remito</code> en la base de datos.
     * @param remito es la <code>Remito</code> que se persistirá.
     */
    public void crearRemito(Remito remito) {
        if (remito.getLotesDelRemito() == null) {
            remito.setLotesDelRemito(new ArrayList<LoteRemito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LoteRemito> attachedLotes = new ArrayList<LoteRemito>();
            for (LoteRemito lotesLoteRemitoToAttach : remito.getLotesDelRemito()) {
                lotesLoteRemitoToAttach = em.getReference(lotesLoteRemitoToAttach.getClass(), lotesLoteRemitoToAttach.getId());
                attachedLotes.add(lotesLoteRemitoToAttach);
            }
            remito.setLotesDelRemito(attachedLotes);
            em.persist(remito);
            for (LoteRemito lotesLoteRemito : remito.getLotesDelRemito()) {
                Remito oldRemitoOfLotesLoteRemito = lotesLoteRemito.getRemito();
                lotesLoteRemito.setRemito(remito);
                lotesLoteRemito = em.merge(lotesLoteRemito);
                if (oldRemitoOfLotesLoteRemito != null) {
                    oldRemitoOfLotesLoteRemito.getLotesDelRemito().remove(lotesLoteRemito);
                    oldRemitoOfLotesLoteRemito = em.merge(oldRemitoOfLotesLoteRemito);
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
     * Actualiza un objeto <code>Remito</code> en la base de datos.
     * @param remito es el <code>Remito</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el remito que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarRemito(Remito remito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito persistentRemito = em.find(Remito.class, remito.getId());
            List<LoteRemito> lotesOld = persistentRemito.getLotesDelRemito();
            List<LoteRemito> lotesNew = remito.getLotesDelRemito();
            List<LoteRemito> attachedLotesNew = new ArrayList<LoteRemito>();
            for (LoteRemito lotesNewLoteRemitoToAttach : lotesNew) {
                lotesNewLoteRemitoToAttach = em.getReference(lotesNewLoteRemitoToAttach.getClass(), lotesNewLoteRemitoToAttach.getId());
                attachedLotesNew.add(lotesNewLoteRemitoToAttach);
            }
            lotesNew = attachedLotesNew;
            remito.setLotesDelRemito(lotesNew);
            remito = em.merge(remito);
            for (LoteRemito lotesOldLoteRemito : lotesOld) {
                if (!lotesNew.contains(lotesOldLoteRemito)) {
                    lotesOldLoteRemito.setRemito(null);
                    lotesOldLoteRemito = em.merge(lotesOldLoteRemito);
                }
            }
            for (LoteRemito lotesNewLoteRemito : lotesNew) {
                if (!lotesOld.contains(lotesNewLoteRemito)) {
                    Remito oldRemitoOfLotesNewLoteRemito = lotesNewLoteRemito.getRemito();
                    lotesNewLoteRemito.setRemito(remito);
                    lotesNewLoteRemito = em.merge(lotesNewLoteRemito);
                    if (oldRemitoOfLotesNewLoteRemito != null && !oldRemitoOfLotesNewLoteRemito.equals(remito)) {
                        oldRemitoOfLotesNewLoteRemito.getLotesDelRemito().remove(lotesNewLoteRemito);
                        oldRemitoOfLotesNewLoteRemito = em.merge(oldRemitoOfLotesNewLoteRemito);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = remito.getId();
                if (encontrarRemito(id) == null) {
                    throw new NonexistentEntityException("The remito with id " + id + " no longer exists.");
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
     * Elimina un remito de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del remito en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el remito que se quiere eliminar no existe en la base de datos.
     */
    public void destruirRemito(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito aRemito;
            try {
                aRemito = em.getReference(Remito.class, id);
                aRemito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The remito with id " + id + " no longer exists.", enfe);
            }
            List<LoteRemito> lotes = aRemito.getLotesDelRemito();
            for (LoteRemito lotesLoteRemito : lotes) {
                lotesLoteRemito.setRemito(null);
                lotesLoteRemito = em.merge(lotesLoteRemito);
            }
            em.remove(aRemito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Devuelve un objeto <code>Remito</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del remito en la base de datos.
     * @return remito
     */
    public Remito encontrarRemito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Remito.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de remitos registrados en la base de datos.
     * @return cantidad
     */
    public int getRemitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Remito> rt = cq.from(Remito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Agrega un lote nuevo al remito en curso.
     * @param codigoLote es el codigo del lote.
     * @param codigoProducto es el codigo del producto.
     * @param fechaProduccion es la fecha de produccion del lote de productos.
     * @param fechaVencimiento es la fecha de vencimiento del lote de productos.
     * @param cantidad es la cantidad de productos en el lote.
     * @throws CodigoProductoNoRegistradoException Se lanza si el codigo del producto indicado no está registrado en la base de datos.
     * @throws Exception Se lanza si el codigo del lote ya está registrado o si el codigo del producto no está registrado.
     */
    public void agregarLote(String codigoLote, String codigoProducto, Date fechaProduccion, Date fechaVencimiento, Double cantidad) throws CodigoProductoNoRegistradoException, Exception {
        if (loteJpaController.codigoLoteDisponible(codigoLote) && codigoNoSeAgrego(codigoLote)) {
            Producto producto = productoJpaController.buscarProductoPorCodigo(codigoProducto);

            Lote lote = new Lote();
            lote.setCodigo(codigoLote);
            lote.setProducto(producto);
            lote.setFechaProduccion(fechaProduccion);
            lote.setFechaVencimiento(fechaVencimiento);

            LoteRemito loteRemito = new LoteRemito();
            loteRemito.setLote(lote);
            loteRemito.setRemito(null);
            loteRemito.setCantidadIngresada(cantidad);
            getLotesDelRemito().add(loteRemito);

            notificarCambios();
        }
    }

    /**
     * Elimina un lote del remito en curso.
     * @param loteRemito es el remito a eliminar.
     */
    public void eliminarLote(LoteRemito loteRemito) {
        getLotesDelRemito().remove(loteRemito);
    }

    /**
     * Se registran los datos del remito.
     * @param codigoRemito es el codigo del remito.
     * @param fecha es la fecha del remito.
     * @throws Exception Se lanza si en el formulario no ha seleccionado un almacén
     */
    public void registrarDatosRemito(String codigoRemito, Date fecha) throws Exception {
        if (almacen != null) {
                this.remito = new Remito();
                this.remito.setCodigo(codigoRemito);
                this.remito.setFecha(fecha);
        } else {
            throw new Exception("No ha seleccionado ningún almacén");
        }
    }

    /**
     * Persiste los datos del remito y los lotes asociados al remito.
     */
    public void persistirOperacion() {
        crearRemito(remito);
        Iterator<LoteRemito> i = getLotesDelRemito().iterator();
        while(i.hasNext()) {
            LoteRemito loteRemito = i.next();

            Lote lote = loteRemito.getLote();
            loteJpaController.crearLote(lote);

            loteRemito.setRemito(remito);
            loteJpaController.crearLoteRemito(loteRemito);

            Double cantidad = loteRemito.getCantidadIngresada();
            LoteAlmacenado loteAlmacenado = new LoteAlmacenado();
            loteAlmacenado.setAlmacen(almacen);
            loteAlmacenado.setLote(lote);
            loteAlmacenado.setCantidad(cantidad);
            loteJpaController.crearLoteAlmacenado(loteAlmacenado);
        }
        operacionJpaController.registrarOperacionIngreso(ControllerSingleton.getEmpleadoJpaController().getEmpleadoQueInicioSesion(), remito);
        this.lotesDelRemito = new ArrayList();
        this.remito = null;
        notificarCambios();
    }

    /**
     * Cancela la importación de productos.
     */
    public void cancelarIngresoDeLotesDeProductos() {
        limpiarVariables();
    }

    private Boolean codigoNoSeAgrego(String codigo) {
        Iterator<LoteRemito> i = getLotesDelRemito().iterator();
        while(i.hasNext())
            if(i.next().getLote().getCodigo().equals(codigo))
                return false;
        return true;
    }

    private void limpiarVariables() {
        remito = null;
        setAlmacen(null);
        lotesDelRemito = new ArrayList();
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    /**
     * @return the lotesDelRemito
     */
    public ArrayList<LoteRemito> getLotesDelRemito() {
        return lotesDelRemito;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

}
