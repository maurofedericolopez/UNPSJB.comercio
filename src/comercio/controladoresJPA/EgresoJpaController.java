package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Egreso;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import comercio.modelo.LoteEgresado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro
 */
public class EgresoJpaController implements Serializable {

    public EgresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Egreso egreso) {
        if (egreso.getLotesEgresados() == null) {
            egreso.setLotesEgresados(new ArrayList<LoteEgresado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LoteEgresado> attachedLotesEgresados = new ArrayList<LoteEgresado>();
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

    public void edit(Egreso egreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso persistentEgreso = em.find(Egreso.class, egreso.getId());
            List<LoteEgresado> lotesEgresadosOld = persistentEgreso.getLotesEgresados();
            List<LoteEgresado> lotesEgresadosNew = egreso.getLotesEgresados();
            List<LoteEgresado> attachedLotesEgresadosNew = new ArrayList<LoteEgresado>();
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
                if (findEgreso(id) == null) {
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso egreso;
            try {
                egreso = em.getReference(Egreso.class, id);
                egreso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The egreso with id " + id + " no longer exists.", enfe);
            }
            List<LoteEgresado> lotesEgresados = egreso.getLotesEgresados();
            for (LoteEgresado lotesEgresadosLoteEgresado : lotesEgresados) {
                lotesEgresadosLoteEgresado.setEgreso(null);
                lotesEgresadosLoteEgresado = em.merge(lotesEgresadosLoteEgresado);
            }
            em.remove(egreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Egreso> findEgresoEntities() {
        return findEgresoEntities(true, -1, -1);
    }

    public List<Egreso> findEgresoEntities(int maxResults, int firstResult) {
        return findEgresoEntities(false, maxResults, firstResult);
    }

    private List<Egreso> findEgresoEntities(boolean all, int maxResults, int firstResult) {
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

    public Egreso findEgreso(Long id) {
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

}
