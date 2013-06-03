package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;
import modelo.*;

/**
 * Ésta clase se encarga de las operaciones CRUD de la entidad <code>Almacen</code>.
 * También es responsable de los movimientos de inventario en almacenes.
 * @author Mauro Federico Lopez
 */
public class AlmacenJpaController implements Serializable {

    private LoteJpaController loteJpaController;
    private OperacionJpaController operacionJpaController;

    /**
     * Construye un nuevo controlador para la entidad <code>Almacen</code>.
     */
    public AlmacenJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
        loteJpaController = new LoteJpaController();
        operacionJpaController = new OperacionJpaController();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Almacen</code> en la base de datos.
     * @param almacen es el <code>Almacen</code> que se persistirá.
     */
    public void crearAlmacen(Almacen almacen) {
        if (almacen.getLotesAlmacenados() == null) {
            almacen.setLotesAlmacenados(new ArrayList<LoteAlmacenado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal sucursal = almacen.getSucursal();
            if (sucursal != null) {
                sucursal = em.getReference(sucursal.getClass(), sucursal.getId());
                almacen.setSucursal(sucursal);
            }
            List<LoteAlmacenado> attachedLotesAlmacenados = new ArrayList();
            for (LoteAlmacenado lotesAlmacenadosLoteAlmacenadoToAttach : almacen.getLotesAlmacenados()) {
                lotesAlmacenadosLoteAlmacenadoToAttach = em.getReference(lotesAlmacenadosLoteAlmacenadoToAttach.getClass(), lotesAlmacenadosLoteAlmacenadoToAttach.getId());
                attachedLotesAlmacenados.add(lotesAlmacenadosLoteAlmacenadoToAttach);
            }
            almacen.setLotesAlmacenados(attachedLotesAlmacenados);
            em.persist(almacen);
            if (sucursal != null) {
                sucursal.getAlmacenes().add(almacen);
                sucursal = em.merge(sucursal);
            }
            for (LoteAlmacenado lotesAlmacenadosLoteAlmacenado : almacen.getLotesAlmacenados()) {
                Almacen oldAlmacenOfLotesAlmacenadosLoteAlmacenado = lotesAlmacenadosLoteAlmacenado.getAlmacen();
                lotesAlmacenadosLoteAlmacenado.setAlmacen(almacen);
                lotesAlmacenadosLoteAlmacenado = em.merge(lotesAlmacenadosLoteAlmacenado);
                if (oldAlmacenOfLotesAlmacenadosLoteAlmacenado != null) {
                    oldAlmacenOfLotesAlmacenadosLoteAlmacenado.getLotesAlmacenados().remove(lotesAlmacenadosLoteAlmacenado);
                    oldAlmacenOfLotesAlmacenadosLoteAlmacenado = em.merge(oldAlmacenOfLotesAlmacenadosLoteAlmacenado);
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
     * Actualiza un objeto <code>Almacen</code> en la base de datos.
     * @param almacen es el <code>Almacen</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el almacen que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarAlmacen(Almacen almacen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen persistentAlmacen = em.find(Almacen.class, almacen.getId());
            Sucursal sucursalOld = persistentAlmacen.getSucursal();
            Sucursal sucursalNew = almacen.getSucursal();
            List<LoteAlmacenado> lotesAlmacenadosOld = persistentAlmacen.getLotesAlmacenados();
            List<LoteAlmacenado> lotesAlmacenadosNew = almacen.getLotesAlmacenados();
            if (sucursalNew != null) {
                sucursalNew = em.getReference(sucursalNew.getClass(), sucursalNew.getId());
                almacen.setSucursal(sucursalNew);
            }
            List<LoteAlmacenado> attachedLotesAlmacenadosNew = new ArrayList();
            for (LoteAlmacenado lotesAlmacenadosNewLoteAlmacenadoToAttach : lotesAlmacenadosNew) {
                lotesAlmacenadosNewLoteAlmacenadoToAttach = em.getReference(lotesAlmacenadosNewLoteAlmacenadoToAttach.getClass(), lotesAlmacenadosNewLoteAlmacenadoToAttach.getId());
                attachedLotesAlmacenadosNew.add(lotesAlmacenadosNewLoteAlmacenadoToAttach);
            }
            lotesAlmacenadosNew = attachedLotesAlmacenadosNew;
            almacen.setLotesAlmacenados(lotesAlmacenadosNew);
            almacen = em.merge(almacen);
            if (sucursalOld != null && !sucursalOld.equals(sucursalNew)) {
                sucursalOld.getAlmacenes().remove(almacen);
                sucursalOld = em.merge(sucursalOld);
            }
            if (sucursalNew != null && !sucursalNew.equals(sucursalOld)) {
                sucursalNew.getAlmacenes().add(almacen);
                sucursalNew = em.merge(sucursalNew);
            }
            for (LoteAlmacenado lotesAlmacenadosOldLoteAlmacenado : lotesAlmacenadosOld) {
                if (!lotesAlmacenadosNew.contains(lotesAlmacenadosOldLoteAlmacenado)) {
                    lotesAlmacenadosOldLoteAlmacenado.setAlmacen(null);
                    lotesAlmacenadosOldLoteAlmacenado = em.merge(lotesAlmacenadosOldLoteAlmacenado);
                }
            }
            for (LoteAlmacenado lotesAlmacenadosNewLoteAlmacenado : lotesAlmacenadosNew) {
                if (!lotesAlmacenadosOld.contains(lotesAlmacenadosNewLoteAlmacenado)) {
                    Almacen oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado = lotesAlmacenadosNewLoteAlmacenado.getAlmacen();
                    lotesAlmacenadosNewLoteAlmacenado.setAlmacen(almacen);
                    lotesAlmacenadosNewLoteAlmacenado = em.merge(lotesAlmacenadosNewLoteAlmacenado);
                    if (oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado != null && !oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado.equals(almacen)) {
                        oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado.getLotesAlmacenados().remove(lotesAlmacenadosNewLoteAlmacenado);
                        oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado = em.merge(oldAlmacenOfLotesAlmacenadosNewLoteAlmacenado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = almacen.getId();
                if (encontrarAlmacen(id) == null) {
                    throw new NonexistentEntityException("El almacén que desea actualizar en la base de datos no existe.");
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
     * Elimina un almacen de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del almacen en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el almacen que se quiere eliminar no existe en la base de datos.
     */
    public void destruirAlmacen(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen almacen;
            try {
                almacen = em.getReference(Almacen.class, id);
                almacen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El almacen que desea eliminar de la base de datos no existe.", enfe);
            }
            Sucursal sucursal = almacen.getSucursal();
            if (sucursal != null) {
                sucursal.getAlmacenes().remove(almacen);
                sucursal = em.merge(sucursal);
            }
            List<LoteAlmacenado> lotesAlmacenados = almacen.getLotesAlmacenados();
            for (LoteAlmacenado lotesAlmacenadosLoteAlmacenado : lotesAlmacenados) {
                lotesAlmacenadosLoteAlmacenado.setAlmacen(null);
                lotesAlmacenadosLoteAlmacenado = em.merge(lotesAlmacenadosLoteAlmacenado);
            }
            em.remove(almacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Devuelve un objeto <code>Almacen</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del almacen en la base de datos.
     * @return almacen
     */
    public Almacen encontrarAlmacen(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de almacenes registrados en la base de datos.
     * @return cantidad
     */
    public int getAlmacenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Almacen> rt = cq.from(Almacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista de todos los almacenes registrados en la base de datos.
     * Devuelve un ArrayList de almacenes.
     * @return almacenes
     */
    public ArrayList<Almacen> obtenerTodosLosAlmacenes() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacen.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Almacen> almacenes = new ArrayList();
            for(Object o : array)
                almacenes.add((Almacen) o);
            return almacenes;
        } catch (NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    /**
     * Descuenta de un lote de un almacen la cantidad indicada.
     * Si el almacén no contiene el lote, lanza una excepción.
     * Si el almacen si contiene el lote pero la cantidad que se requiere transferir es mayor al contenido en el lote, lanza una excepción.
     * @param almacen el <code>Almacen</code> donde se modificará el inventario.
     * @param lote el <code>Lote</code> de donde se descontará.
     * @param cantidad la cantidad que se descontará.
     * @throws Exception Se lanza si no hay cantidad suficiente para descontar o si el almacen no contiene al lote especificado.
     */
    public void descontarDeAlmacen(Almacen almacen, Lote lote, Double cantidad) throws Exception {
        LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, lote);
        if(loteAlmacenado != null) {
            if(loteAlmacenado.getCantidad() >= cantidad) {
                Double cantidadNueva = loteAlmacenado.getCantidad() - cantidad;
                loteAlmacenado.setCantidad(cantidadNueva);
                loteJpaController.editarLoteAlmacenado(loteAlmacenado);
            } else {
                throw new Exception("No hay cantidad suficiente para satisfacer la transferencia.");
            }
        } else {
            throw new Exception("El almacén no contienen ningun lote con el código de lote ingresado.");
        }
    }

    /**
     * Incrementa la cantidad de lote en el almacen especificado.
     * Si el lote no está en el almacen, se agrega.
     * @param almacen el <code>Almacen</code> donde se modificará el inventario.
     * @param lote el <code>Lote</code> donde se incrementará.
     * @param cantidad la cantidad que se requiere incrementar.
     * @throws Exception Se lanza si ocurre un error en la base de datos.
     */
    public void aumentarStockEnAlmacen(Almacen almacen, Lote lote, Double cantidad) throws Exception {
        LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, lote);
        if(loteAlmacenado != null) {
            Double cantidadNueva = loteAlmacenado.getCantidad() + cantidad;
            loteAlmacenado.setCantidad(cantidadNueva);
            loteJpaController.editarLoteAlmacenado(loteAlmacenado);
        } else {
            LoteAlmacenado nuevoLoteAlmacenado = new LoteAlmacenado();
            nuevoLoteAlmacenado.setAlmacen(almacen);
            nuevoLoteAlmacenado.setLote(lote);
            nuevoLoteAlmacenado.setCantidad(cantidad);
            loteJpaController.crearLoteAlmacenado(nuevoLoteAlmacenado);
        }
    }

    /**
     * Modifica la cantidad del lote en el almacen especificado.
     * @param almacen el <code>Almacen</code> donde se modificará el inventario.
     * @param codigoLote es el codigo del lote para buscar en la base de datos.
     * @param cantidad es la cantidad con la que se quiere reemplazar al viejo valor.
     * @throws Exception Se lanza si el codigo del lote no está registrado en la base de datos, o si el almacen no contiene al lote especificado.
     */
    public void corregirLoteAlmacenado(Almacen almacen, String codigoLote, Double cantidad) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, lote);
        if(loteAlmacenado != null) {
            String msg = "El punto de venta tiene una cantidad de " + loteAlmacenado.getCantidad() + " del producto " + codigoLote.toUpperCase() + "."
                    + "\n¿Desea reemplazar por la cantidad " + cantidad + "?";
            int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Corregir Inventario", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(showOptionDialog == 0) {
                loteAlmacenado.setCantidad(cantidad);
                loteJpaController.editarLoteAlmacenado(loteAlmacenado);
                operacionJpaController.registrarOperacionCorreccionDeInventario(ControllerSingleton.getEmpleadoJpaController().getEmpleadoQueInicioSesion(), loteAlmacenado);
            }
        } else {
            throw new Exception("El almacén no contiene este lote.");
        }
    }

    /**
     * Devuelve la cantidad de productos que existen en el almacen especificado.
     * @param almacen el <code>Almacen</code> donde se buscará el producto.
     * @param producto el <code>Producto</code> que se buscará.
     * @return cantidad.
     */
    public Double cantidadDeProductosEnAlmacen(Almacen almacen, Producto producto) {
        Iterator<Lote> i = loteJpaController.obtenerLotesPorProducto(producto).iterator();
        Double cantidad = 0.0;
        while(i.hasNext()) {
            LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, i.next());
            if(loteAlmacenado != null)
                cantidad += loteAlmacenado.getCantidad();
        }
        return cantidad;
    }

    /**
     * Devuelve una lista con todos los lotes que están próximos a vencer en un almacén especificado.
     * @param almacen el <code>Almacen</code> donde se buscarán los lotes.
     * @return lotesProximosAVencer.
     */
    public ArrayList<LoteAlmacenado> obtenerLotesProximosAVencerDeAlmacen(Almacen almacen) {
        ArrayList<LoteAlmacenado> lotesAlmacenados = new ArrayList();
        Iterator<Lote> i = loteJpaController.obtenerLotesProximosAVencer().iterator();
        while(i.hasNext()) {
            LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, i.next());
            if(loteAlmacenado != null) {
                lotesAlmacenados.add(loteAlmacenado);
            }
        }
        return lotesAlmacenados;
    }

}
