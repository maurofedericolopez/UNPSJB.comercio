package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.LoteRemito;
import comercio.modelo.Remito;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class RemitoJpaController implements Serializable {

    public RemitoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Remito remito) {
        if (remito.getLotes() == null) {
            remito.setLotes(new ArrayList<LoteRemito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LoteRemito> attachedLotes = new ArrayList<LoteRemito>();
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

    public void edit(Remito remito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito persistentRemito = em.find(Remito.class, remito.getId());
            List<LoteRemito> lotesOld = persistentRemito.getLotes();
            List<LoteRemito> lotesNew = remito.getLotes();
            List<LoteRemito> attachedLotesNew = new ArrayList<LoteRemito>();
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
                if (findRemito(id) == null) {
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito remito;
            try {
                remito = em.getReference(Remito.class, id);
                remito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The remito with id " + id + " no longer exists.", enfe);
            }
            List<LoteRemito> lotes = remito.getLotes();
            for (LoteRemito lotesLoteRemito : lotes) {
                lotesLoteRemito.setRemito(null);
                lotesLoteRemito = em.merge(lotesLoteRemito);
            }
            em.remove(remito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Remito> findRemitoEntities() {
        return findRemitoEntities(true, -1, -1);
    }

    public List<Remito> findRemitoEntities(int maxResults, int firstResult) {
        return findRemitoEntities(false, maxResults, firstResult);
    }

    private List<Remito> findRemitoEntities(boolean all, int maxResults, int firstResult) {
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

    public Remito findRemito(Long id) {
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

}
