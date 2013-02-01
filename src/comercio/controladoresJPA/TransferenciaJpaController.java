package comercio.controladoresJPA;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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

    public ArrayList<Transferencia> buscarTransferenciasPorAlmacenOrigen(Almacen almacenOrigen, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transferencia> cq = cb.createQuery(Transferencia.class);
            Root<Transferencia> trans = cq.from(Transferencia.class);
            cq.select(trans);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(trans.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (almacenOrigen != null) {
                almacenPredicate = cb.equal(trans.get("almacenOrigen"), almacenOrigen);
                predicateList.add(almacenPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            ArrayList<Transferencia> transferencias = new ArrayList();
            Object[] array = em.createQuery(cq).getResultList().toArray();
            for(Object o : array)
                transferencias.add((Transferencia) o);
            return transferencias;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<Transferencia> buscarTransferenciasPorAlmacenDestino(Almacen almacenDestino, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transferencia> cq = cb.createQuery(Transferencia.class);
            Root<Transferencia> trans = cq.from(Transferencia.class);
            cq.select(trans);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenPredicate, puntoDeVentaPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(trans.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (almacenDestino != null) {
                almacenPredicate = cb.equal(trans.get("almacenDestino"), almacenDestino);
                predicateList.add(almacenPredicate);
            }

            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            ArrayList<Transferencia> transferencias = new ArrayList();
            Object[] array = em.createQuery(cq).getResultList().toArray();
            for(Object o : array)
                transferencias.add((Transferencia) o);
            return transferencias;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<Transferencia> buscarTransferenciasPorPuntoDeVentaDestino(PuntoVenta puntoDeVentaDestino, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transferencia> cq = cb.createQuery(Transferencia.class);
            Root<Transferencia> trans = cq.from(Transferencia.class);
            cq.select(trans);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(trans.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (puntoDeVentaDestino != null) {
                almacenPredicate = cb.equal(trans.get("puntoDeVentaDestino"), puntoDeVentaDestino);
                predicateList.add(almacenPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            ArrayList<Transferencia> transferencias = new ArrayList();
            Object[] array = em.createQuery(cq).getResultList().toArray();
            for(Object o : array)
                transferencias.add((Transferencia) o);
            return transferencias;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public Transferencia buscarTransferenciaPorAlmacenOrigenDestino(Almacen almacenOrigen, Almacen almacenDestino, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transferencia> cq = cb.createQuery(Transferencia.class);
            Root<Transferencia> trans = cq.from(Transferencia.class);
            cq.select(trans);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenOrigenPredicate, almacenDestinoPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(trans.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (almacenOrigen != null) {
                almacenOrigenPredicate = cb.equal(trans.get("almacenOrigen"), almacenOrigen);
                predicateList.add(almacenOrigenPredicate);
            }

            if (almacenDestino != null) {
                almacenDestinoPredicate = cb.equal(trans.get("almacenDestino"), almacenDestino);
                predicateList.add(almacenDestinoPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            return (Transferencia) em.createQuery(cq).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public Transferencia buscarTransferenciaPuntoDeVentaDestino(Almacen almacenOrigen, PuntoVenta puntoDeVentaDestino, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Transferencia> cq = cb.createQuery(Transferencia.class);
            Root<Transferencia> trans = cq.from(Transferencia.class);
            cq.select(trans);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenOrigenPredicate, puntoDeVentaDestinoPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(trans.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (almacenOrigen != null) {
                almacenOrigenPredicate = cb.equal(trans.get("almacenOrigen"), almacenOrigen);
                predicateList.add(almacenOrigenPredicate);
            }

            if (puntoDeVentaDestino != null) {
                puntoDeVentaDestinoPredicate = cb.equal(trans.get("puntoDeVentaDestino"), puntoDeVentaDestino);
                predicateList.add(puntoDeVentaDestinoPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            return (Transferencia) em.createQuery(cq).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public void transferirProductosAVenta(String codigoLote, Double cantidad, Almacen almacen, PuntoVenta puntoDeVenta) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacen, lote, cantidad);
        Producto producto = lote.getProducto();
        puntoVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);

        Transferencia transferencia = buscarTransferenciaPuntoDeVentaDestino(almacen, puntoDeVenta, lote);
        if(transferencia != null) {
            Double cantidadNueva = transferencia.getCantidad() + cantidad;
            transferencia.setCantidad(cantidadNueva);
        } else {
            transferencia = new Transferencia();
            transferencia.setAlmacenOrigen(almacen);
            transferencia.setAlmacenDestino(null);
            transferencia.setPuntoDeVentaDestino(puntoDeVenta);
            transferencia.setCantidad(cantidad);
            crearTransferencia(transferencia);
        }
    }

    public void transferirProductosAlmacen(String codigoLote, Double cantidad, Almacen almacenOrigen, Almacen almacenDestino) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacenOrigen, lote, cantidad);
        almacenJpaController.aumentarStockEnAlmacen(almacenDestino, lote, cantidad);

        Transferencia transferencia = buscarTransferenciaPorAlmacenOrigenDestino(almacenOrigen, almacenDestino, lote);
        if(transferencia != null) {
            Double cantidadNueva = transferencia.getCantidad() + cantidad;
            transferencia.setCantidad(cantidadNueva);
        } else {
            transferencia = new Transferencia();
            transferencia.setAlmacenOrigen(almacenOrigen);
            transferencia.setAlmacenDestino(almacenDestino);
            transferencia.setPuntoDeVentaDestino(null);
            transferencia.setCantidad(cantidad);
            crearTransferencia(transferencia);
        }
    }

    public void cancelarTransferenciaAlmacen() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void cancelarTransferenciaAVenta() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void descontarPuntoDeVenta(PuntoVenta puntoDeVenta, Double cantidad) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<Transferencia> obtenerTransferenciasProximasAVencerDeAlmacen(Almacen almacen) {
        ArrayList<Transferencia> transferencias = new ArrayList();
        Iterator<Lote> i = loteJpaController.obtenerLotesProximosAVencer().iterator();
        while(i.hasNext()) {
            transferencias.addAll(buscarTransferenciasPorAlmacenDestino(almacen, i.next()));
        }
        return transferencias;
    }

    public ArrayList<Transferencia> obtenerTransferenciasProximasAVencerDePuntoDeVenta(PuntoVenta puntoDeVenta) {
        ArrayList<Transferencia> transferencias = new ArrayList();
        Iterator<Lote> i = loteJpaController.obtenerLotesProximosAVencer().iterator();
        while(i.hasNext()) {
            transferencias.addAll(buscarTransferenciasPorPuntoDeVentaDestino(puntoDeVenta, i.next()));
        }
        return transferencias;
    }

}
