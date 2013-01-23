package comercio.controladoresJPA;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.io.Serializable;
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
public class TransferenciaJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    private LoteJpaController loteJpaController;
    private AlmacenJpaController almacenJpaController;
    private PuntoVentaJpaController puntoVentaJpaController;

    public TransferenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        loteJpaController = ControllerSingleton.getLoteJpaController();
        almacenJpaController = ControllerSingleton.getAlmacenJpaController();
        puntoVentaJpaController = ControllerSingleton.getPuntoVentaJpaController();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearTransferencia(Transferencia transferencia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(transferencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarTransferencia(Transferencia transferencia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            transferencia = em.merge(transferencia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = transferencia.getId();
                if (encontrarTransferencia(id) == null) {
                    throw new NonexistentEntityException("The transferencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirTransferencia(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transferencia transferencia;
            try {
                transferencia = em.getReference(Transferencia.class, id);
                transferencia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transferencia with id " + id + " no longer exists.", enfe);
            }
            em.remove(transferencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transferencia> encontrarTransferenciaEntities() {
        return encontrarTransferenciaEntities(true, -1, -1);
    }

    public List<Transferencia> encontrarTransferenciaEntities(int maxResults, int firstResult) {
        return encontrarTransferenciaEntities(false, maxResults, firstResult);
    }

    private List<Transferencia> encontrarTransferenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transferencia.class));
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

    public Transferencia encontrarTransferencia(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transferencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransferenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transferencia> rt = cq.from(Transferencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void transferirProductosAVenta(String codigoLote, Double cantidad, Almacen almacen, PuntoVenta puntoDeVenta) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacen, codigoLote, cantidad);
        Producto producto = lote.getProducto();
        puntoVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);

        Transferencia transferencia = new Transferencia();
        transferencia.setAlmacenOrigen(almacen);
        transferencia.setPuntoDeVentaDestino(puntoDeVenta);
        transferencia.setCantidad(cantidad);
        crearTransferencia(transferencia);
    }

    public void transferirProductosAlmacen(String codigoLote, Double cantidad, Almacen almacenOrigen, Almacen almacenDestino) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacenOrigen, codigoLote, cantidad);
        almacenJpaController.aumentarStockEnAlmacen(almacenDestino, lote, cantidad);

        Transferencia transferencia = new Transferencia();
        transferencia.setAlmacenOrigen(almacenOrigen);
        transferencia.setAlmacenDestino(almacenDestino);
        transferencia.setCantidad(cantidad);
        crearTransferencia(transferencia);
    }

    public void cancelarTransferenciaAlmacen() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void cancelarTransferenciaAVenta() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
