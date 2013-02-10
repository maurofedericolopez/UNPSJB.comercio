package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class EgresoJpaController extends Observable implements Serializable {

    private EntityManagerFactory emf = null;
    private LoteJpaController loteJpaController;
    private AlmacenJpaController almacenJpaController;

    private Almacen almacen = null;
    private ArrayList<LoteEgresado> lotesEgresados = new ArrayList();
    private Egreso egreso;

    public EgresoJpaController() {
        this.emf = ControllerSingleton.getEmf();
        loteJpaController = ControllerSingleton.getLoteJpaController();
        almacenJpaController = ControllerSingleton.getAlmacenJpaController();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearEgreso(Egreso egreso) {
        if (egreso.getLotesEgresados() == null) {
            egreso.setLotesEgresados(new ArrayList());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LoteEgresado> attachedLotesEgresados = new ArrayList();
            for (LoteEgresado lotesEgresadosLoteEgresadoToAttach : egreso.getLotesEgresados()) {
                lotesEgresadosLoteEgresadoToAttach = em.getReference(lotesEgresadosLoteEgresadoToAttach.getClass(), lotesEgresadosLoteEgresadoToAttach.getId());
                attachedLotesEgresados.add(lotesEgresadosLoteEgresadoToAttach);
            }
            egreso.setLotesEgresados(attachedLotesEgresados);
            em.persist(egreso);
            for (LoteEgresado lotesEgresadosLoteEgresado : egreso.getLotesEgresados()) {
                Egreso oldEgresoOfLotesEgresadosLoteEgresado = lotesEgresadosLoteEgresado.getEgreso();
                lotesEgresadosLoteEgresado.setEgreso(egreso);
                lotesEgresadosLoteEgresado = em.merge(lotesEgresadosLoteEgresado);
                if (oldEgresoOfLotesEgresadosLoteEgresado != null) {
                    oldEgresoOfLotesEgresadosLoteEgresado.getLotesEgresados().remove(lotesEgresadosLoteEgresado);
                    oldEgresoOfLotesEgresadosLoteEgresado = em.merge(oldEgresoOfLotesEgresadosLoteEgresado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarEgreso(Egreso egreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso persistentEgreso = em.find(Egreso.class, egreso.getId());
            List<LoteEgresado> lotesEgresadosOld = persistentEgreso.getLotesEgresados();
            List<LoteEgresado> lotesEgresadosNew = egreso.getLotesEgresados();
            List<LoteEgresado> attachedLotesEgresadosNew = new ArrayList();
            for (LoteEgresado lotesEgresadosNewLoteEgresadoToAttach : lotesEgresadosNew) {
                lotesEgresadosNewLoteEgresadoToAttach = em.getReference(lotesEgresadosNewLoteEgresadoToAttach.getClass(), lotesEgresadosNewLoteEgresadoToAttach.getId());
                attachedLotesEgresadosNew.add(lotesEgresadosNewLoteEgresadoToAttach);
            }
            lotesEgresadosNew = attachedLotesEgresadosNew;
            egreso.setLotesEgresados(lotesEgresadosNew);
            egreso = em.merge(egreso);
            for (LoteEgresado lotesEgresadosOldLoteEgresado : lotesEgresadosOld) {
                if (!lotesEgresadosNew.contains(lotesEgresadosOldLoteEgresado)) {
                    lotesEgresadosOldLoteEgresado.setEgreso(null);
                    lotesEgresadosOldLoteEgresado = em.merge(lotesEgresadosOldLoteEgresado);
                }
            }
            for (LoteEgresado lotesEgresadosNewLoteEgresado : lotesEgresadosNew) {
                if (!lotesEgresadosOld.contains(lotesEgresadosNewLoteEgresado)) {
                    Egreso oldEgresoOfLotesEgresadosNewLoteEgresado = lotesEgresadosNewLoteEgresado.getEgreso();
                    lotesEgresadosNewLoteEgresado.setEgreso(egreso);
                    lotesEgresadosNewLoteEgresado = em.merge(lotesEgresadosNewLoteEgresado);
                    if (oldEgresoOfLotesEgresadosNewLoteEgresado != null && !oldEgresoOfLotesEgresadosNewLoteEgresado.equals(egreso)) {
                        oldEgresoOfLotesEgresadosNewLoteEgresado.getLotesEgresados().remove(lotesEgresadosNewLoteEgresado);
                        oldEgresoOfLotesEgresadosNewLoteEgresado = em.merge(oldEgresoOfLotesEgresadosNewLoteEgresado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = egreso.getId();
                if (encontrarEgreso(id) == null) {
                    throw new NonexistentEntityException("The egreso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirEgreso(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso aEgreso;
            try {
                aEgreso = em.getReference(Egreso.class, id);
                aEgreso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The egreso with id " + id + " no longer exists.", enfe);
            }
            List<LoteEgresado> aLotesEgresados = aEgreso.getLotesEgresados();
            for (LoteEgresado lotesEgresadosLoteEgresado : aLotesEgresados) {
                lotesEgresadosLoteEgresado.setEgreso(null);
                lotesEgresadosLoteEgresado = em.merge(lotesEgresadosLoteEgresado);
            }
            em.remove(aEgreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Egreso> encontrarEgresoEntities() {
        return encontrarEgresoEntities(true, -1, -1);
    }

    public List<Egreso> encontrarEgresoEntities(int maxResults, int firstResult) {
        return encontrarEgresoEntities(false, maxResults, firstResult);
    }

    private List<Egreso> encontrarEgresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Egreso.class));
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

    public Egreso encontrarEgreso(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Egreso.class, id);
        } finally {
            em.close();
        }
    }

    public int getEgresoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Egreso> rt = cq.from(Egreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private Boolean codigoEgresoDisponible(String codigo) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Egreso> c = cb.createQuery(Egreso.class);
            Root<Egreso> p = c.from(Egreso.class);
            c.select(p).where(cb.equal(p.get("codigo"), codigo.toUpperCase()));
            Query q = em.createQuery(c);
            Egreso resultado = (Egreso) q.getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        } finally {
            em.close();
        }
    }

    public void agregarLote(String codigoLote, Double cantidad) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, lote);
        if(loteAlmacenado != null) {
            Boolean encontrado = false;
            Iterator<LoteEgresado> i = lotesEgresados.iterator();
            while(i.hasNext()) {
                LoteEgresado le = i.next();
                if(le.getLote().getCodigo().equals(codigoLote)) {
                    if((le.getCantidad() + cantidad) <= loteAlmacenado.getCantidad()) {
                        Double cantidadNueva = le.getCantidad() + cantidad;
                        le.setCantidad(cantidadNueva);
                    } else {
                        throw new Exception("La cantidad de productos del lote indicado no puede satisfacer el egreso del lote.");
                    }
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado) {
                if(cantidad <= loteAlmacenado.getCantidad()) {
                    LoteEgresado loteEgresado = new LoteEgresado();
                    loteEgresado.setLote(lote);
                    loteEgresado.setCantidad(cantidad);
                    lotesEgresados.add(loteEgresado);
                } else {
                    throw new Exception("La cantidad de productos del lote indicado no puede satisfacer el egreso del lote.");
                }
            }
            notificarCambios();
        } else {
            throw new Exception("El almacén no contiene el lote indicado.");
        }
    }

    /**
     * @return the lotesEgresados
     */
    public ArrayList<LoteEgresado> getLotesEgresados() {
        return lotesEgresados;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
        lotesEgresados = new ArrayList();
        notificarCambios();
    }

    private ArrayList<LoteAlmacenado> obtenerLotesAlmacenadosDeAlmacen(Almacen almacen) {
        ArrayList<LoteAlmacenado> la = new ArrayList();
        Object[] array = almacen.getLotesAlmacenados().toArray();
        for(Object o : array)
            la.add((LoteAlmacenado) o);
        return la;
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    public void agregarDatosDelEgreso(String codigo, String causaEspecial, String observaciones) throws Exception {
        if(codigoEgresoDisponible(codigo)) {
            egreso = new Egreso();
            egreso.setCodigo(codigo);
            egreso.setCausaEspecial(causaEspecial);
            egreso.setObservaciones(observaciones);
            egreso.setAlmacen(almacen);
        }
    }

    public void persistirOperacion() throws Exception {
        crearEgreso(egreso);
        Iterator<LoteEgresado> i = lotesEgresados.iterator();
        while(i.hasNext()) {
            LoteEgresado loteEgresado = i.next();
            loteEgresado.setEgreso(egreso);
            loteJpaController.crearLoteEgresado(loteEgresado);
        }
    }

    public ArrayList<Egreso> obtenerTodosLosEgresos() {
        Object[] array = encontrarEgresoEntities().toArray();
        ArrayList<Egreso> egresos = new ArrayList();
        for(Object o : array)
            egresos.add((Egreso) o);
        return egresos;
    }

    public ArrayList<Egreso> obtenerEgresosPorFecha(Date fecha) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Egreso> cq = cb.createQuery(Egreso.class);
            Root<Egreso> root = cq.from(Egreso.class);
            cq.select(root);
            cq.where(cb.equal(root.get("fecha"), fecha));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Egreso> egresos = new ArrayList();
            for(Object o : array)
                egresos.add((Egreso) o);
            return egresos;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public Egreso obtenerEgresoPorCodigo(String codigo) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Egreso> cq = cb.createQuery(Egreso.class);
            Root<Egreso> root = cq.from(Egreso.class);
            cq.select(root);
            cq.where(cb.equal(root.get("codigi"), codigo.toUpperCase()));
            return em.createQuery(cq).getSingleResult();
        } catch(NoResultException ex) {
            throw new Exception("El egreso con el codigo " + codigo + " no está registrado");
        } finally {
            em.close();
        }
    }

    public ArrayList<LoteEgresado> obtenerLotesEgresados(Egreso egreso) {
        Object[] array = egreso.getLotesEgresados().toArray();
        ArrayList<LoteEgresado> egresoLotesEgresados = new ArrayList();
        for(Object o : array)
            egresoLotesEgresados.add((LoteEgresado) o);
        return egresoLotesEgresados;
    }

}
