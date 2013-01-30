package comercio.controladoresJPA;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.exceptions.CodigoProductoNoRegistradoException;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.io.Serializable;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauro
 */
public class RemitoJpaController extends Observable implements Serializable {

    private EntityManagerFactory emf = null;
    private LoteJpaController loteJpaController;
    private ProductoJpaController productoJpaController;

    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();
    private Remito remito = null;
    private Almacen almacen = null;

    public RemitoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        loteJpaController = ControllerSingleton.getLoteJpaController();
        productoJpaController = ControllerSingleton.getProductoJpaController();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearRemito(Remito remito) {
        if (remito.getLotes() == null) {
            remito.setLotes(new ArrayList<LoteRemito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LoteRemito> attachedLotes = new ArrayList<>();
            for (LoteRemito lotesLoteRemitoToAttach : remito.getLotes()) {
                lotesLoteRemitoToAttach = em.getReference(lotesLoteRemitoToAttach.getClass(), lotesLoteRemitoToAttach.getId());
                attachedLotes.add(lotesLoteRemitoToAttach);
            }
            remito.setLotes(attachedLotes);
            em.persist(remito);
            for (LoteRemito lotesLoteRemito : remito.getLotes()) {
                Remito oldRemitoOfLotesLoteRemito = lotesLoteRemito.getRemito();
                lotesLoteRemito.setRemito(remito);
                lotesLoteRemito = em.merge(lotesLoteRemito);
                if (oldRemitoOfLotesLoteRemito != null) {
                    oldRemitoOfLotesLoteRemito.getLotes().remove(lotesLoteRemito);
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

    public void editarRemito(Remito remito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito persistentRemito = em.find(Remito.class, remito.getId());
            List<LoteRemito> lotesOld = persistentRemito.getLotes();
            List<LoteRemito> lotesNew = remito.getLotes();
            List<LoteRemito> attachedLotesNew = new ArrayList<>();
            for (LoteRemito lotesNewLoteRemitoToAttach : lotesNew) {
                lotesNewLoteRemitoToAttach = em.getReference(lotesNewLoteRemitoToAttach.getClass(), lotesNewLoteRemitoToAttach.getId());
                attachedLotesNew.add(lotesNewLoteRemitoToAttach);
            }
            lotesNew = attachedLotesNew;
            remito.setLotes(lotesNew);
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
                        oldRemitoOfLotesNewLoteRemito.getLotes().remove(lotesNewLoteRemito);
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
            List<LoteRemito> lotes = aRemito.getLotes();
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

    public List<Remito> encontrarRemitoEntities() {
        return encontrarRemitoEntities(true, -1, -1);
    }

    public List<Remito> encontrarRemitoEntities(int maxResults, int firstResult) {
        return encontrarRemitoEntities(false, maxResults, firstResult);
    }

    private List<Remito> encontrarRemitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Remito.class));
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

    public Remito encontrarRemito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Remito.class, id);
        } finally {
            em.close();
        }
    }

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

    public void agregarLote(String codigoLote, String codigoProducto, Date fechaProduccion, Date fechaVencimiento, Double cantidad) throws CodigoProductoNoRegistradoException, Exception {
        if (loteJpaController.codigoLoteDisponible(codigoLote) && codigoNoSeAgrego(codigoLote)) {
            Producto producto = productoJpaController.buscarProductoPorCodigo(codigoLote);

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

    public void eliminarLote(LoteRemito loteRemito) {
        getLotesDelRemito().remove(loteRemito);
    }

    public void registrarDatosRemito(Remito remito, Almacen almacen) throws Exception {
        if (almacen != null) {
            if (remito != null) {
                this.remito = remito;
                this.almacen = almacen;
                notificarCambios();
            } else {
                throw new Exception("No ha ingresado la fecha");
            }
        } else {
            throw new Exception("No ha seleccionado ningún almacén");
        }
    }

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
    }

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
        almacen = null;
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

}
