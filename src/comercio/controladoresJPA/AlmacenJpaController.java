package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Almacen;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.Sucursal;
import comercio.modelo.LoteAlmacenado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author Mauro
 */
public class AlmacenJpaController implements Serializable {

    public AlmacenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Almacen almacen) {
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
            List<LoteAlmacenado> attachedLotesAlmacenados = new ArrayList<LoteAlmacenado>();
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

    public void edit(Almacen almacen) throws NonexistentEntityException, Exception {
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
            List<LoteAlmacenado> attachedLotesAlmacenadosNew = new ArrayList<LoteAlmacenado>();
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
                if (findAlmacen(id) == null) {
                    throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen almacen;
            try {
                almacen = em.getReference(Almacen.class, id);
                almacen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.", enfe);
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

    public List<Almacen> findAlmacenEntities() {
        return findAlmacenEntities(true, -1, -1);
    }

    public List<Almacen> findAlmacenEntities(int maxResults, int firstResult) {
        return findAlmacenEntities(false, maxResults, firstResult);
    }

    private List<Almacen> findAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacen.class));
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

    public Almacen findAlmacen(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, id);
        } finally {
            em.close();
        }
    }

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

    public List<Almacen> buscarAlmacenesPorSucursal(Long id) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Query query = em.createQuery("SELECT * FROM almacen WHERE Sucursal_idSucursal = " + id);
            List resultList = query.getResultList();
            return resultList;
        } finally {
            em.close();
        }
    }

}