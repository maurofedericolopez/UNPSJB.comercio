package comercio.controladoresJPA;

import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.io.Serializable;
import java.util.ArrayList;
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
public class LoteJpaController implements Serializable {

    public LoteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearLote(Lote lote) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(lote);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void crearLoteAlmacenado(LoteAlmacenado loteAlmacenado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen almacen = loteAlmacenado.getAlmacen();
            if (almacen != null) {
                almacen = em.getReference(almacen.getClass(), almacen.getId());
                loteAlmacenado.setAlmacen(almacen);
            }
            em.persist(loteAlmacenado);
            if (almacen != null) {
                almacen.getLotesAlmacenados().add(loteAlmacenado);
                almacen = em.merge(almacen);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void crearLoteEgresado(LoteEgresado loteEgresado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Egreso egreso = loteEgresado.getEgreso();
            if (egreso != null) {
                egreso = em.getReference(egreso.getClass(), egreso.getId());
                loteEgresado.setEgreso(egreso);
            }
            em.persist(loteEgresado);
            if (egreso != null) {
                egreso.getLotesEgresados().add(loteEgresado);
                egreso = em.merge(egreso);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void crearLoteRemito(LoteRemito loteRemito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remito remito = loteRemito.getRemito();
            if (remito != null) {
                remito = em.getReference(remito.getClass(), remito.getId());
                loteRemito.setRemito(remito);
            }
            em.persist(loteRemito);
            if (remito != null) {
                remito.getLotes().add(loteRemito);
                remito = em.merge(remito);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarLote(Lote lote) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            lote = em.merge(lote);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lote.getId();
                if (encontrarLote(id) == null) {
                    throw new NonexistentEntityException("The lote with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarLoteAlmacenado(LoteAlmacenado loteAlmacenado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteAlmacenado persistentLoteAlmacenado = em.find(LoteAlmacenado.class, loteAlmacenado.getId());
            Almacen almacenOld = persistentLoteAlmacenado.getAlmacen();
            Almacen almacenNew = loteAlmacenado.getAlmacen();
            if (almacenNew != null) {
                almacenNew = em.getReference(almacenNew.getClass(), almacenNew.getId());
                loteAlmacenado.setAlmacen(almacenNew);
            }
            loteAlmacenado = em.merge(loteAlmacenado);
            if (almacenOld != null && !almacenOld.equals(almacenNew)) {
                almacenOld.getLotesAlmacenados().remove(loteAlmacenado);
                almacenOld = em.merge(almacenOld);
            }
            if (almacenNew != null && !almacenNew.equals(almacenOld)) {
                almacenNew.getLotesAlmacenados().add(loteAlmacenado);
                almacenNew = em.merge(almacenNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteAlmacenado.getId();
                if (encontrarLoteAlmacenado(id) == null) {
                    throw new NonexistentEntityException("The loteAlmacenado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarLoteEgresado(LoteEgresado loteEgresado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteEgresado persistentLoteEgresado = em.find(LoteEgresado.class, loteEgresado.getId());
            Egreso egresoOld = persistentLoteEgresado.getEgreso();
            Egreso egresoNew = loteEgresado.getEgreso();
            if (egresoNew != null) {
                egresoNew = em.getReference(egresoNew.getClass(), egresoNew.getId());
                loteEgresado.setEgreso(egresoNew);
            }
            loteEgresado = em.merge(loteEgresado);
            if (egresoOld != null && !egresoOld.equals(egresoNew)) {
                egresoOld.getLotesEgresados().remove(loteEgresado);
                egresoOld = em.merge(egresoOld);
            }
            if (egresoNew != null && !egresoNew.equals(egresoOld)) {
                egresoNew.getLotesEgresados().add(loteEgresado);
                egresoNew = em.merge(egresoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteEgresado.getId();
                if (encontrarLoteEgresado(id) == null) {
                    throw new NonexistentEntityException("The loteEgresado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarLoteRemito(LoteRemito loteRemito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteRemito persistentLoteRemito = em.find(LoteRemito.class, loteRemito.getId());
            Remito remitoOld = persistentLoteRemito.getRemito();
            Remito remitoNew = loteRemito.getRemito();
            if (remitoNew != null) {
                remitoNew = em.getReference(remitoNew.getClass(), remitoNew.getId());
                loteRemito.setRemito(remitoNew);
            }
            loteRemito = em.merge(loteRemito);
            if (remitoOld != null && !remitoOld.equals(remitoNew)) {
                remitoOld.getLotes().remove(loteRemito);
                remitoOld = em.merge(remitoOld);
            }
            if (remitoNew != null && !remitoNew.equals(remitoOld)) {
                remitoNew.getLotes().add(loteRemito);
                remitoNew = em.merge(remitoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loteRemito.getId();
                if (encontrarLoteRemito(id) == null) {
                    throw new NonexistentEntityException("The loteRemito with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirLote(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lote lote;
            try {
                lote = em.getReference(Lote.class, id);
                lote.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lote with id " + id + " no longer exists.", enfe);
            }
            em.remove(lote);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirLoteAlmacenado(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteAlmacenado loteAlmacenado;
            try {
                loteAlmacenado = em.getReference(LoteAlmacenado.class, id);
                loteAlmacenado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteAlmacenado with id " + id + " no longer exists.", enfe);
            }
            Almacen almacen = loteAlmacenado.getAlmacen();
            if (almacen != null) {
                almacen.getLotesAlmacenados().remove(loteAlmacenado);
                almacen = em.merge(almacen);
            }
            em.remove(loteAlmacenado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirLoteEgresado(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteEgresado loteEgresado;
            try {
                loteEgresado = em.getReference(LoteEgresado.class, id);
                loteEgresado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteEgresado with id " + id + " no longer exists.", enfe);
            }
            Egreso egreso = loteEgresado.getEgreso();
            if (egreso != null) {
                egreso.getLotesEgresados().remove(loteEgresado);
                egreso = em.merge(egreso);
            }
            em.remove(loteEgresado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destruirLoteRemito(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LoteRemito loteRemito;
            try {
                loteRemito = em.getReference(LoteRemito.class, id);
                loteRemito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loteRemito with id " + id + " no longer exists.", enfe);
            }
            Remito remito = loteRemito.getRemito();
            if (remito != null) {
                remito.getLotes().remove(loteRemito);
                remito = em.merge(remito);
            }
            em.remove(loteRemito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lote> encontrarLoteEntities() {
        return encontrarLoteEntities(true, -1, -1);
    }

    public List<LoteAlmacenado> encontrarLoteAlmacenadoEntities() {
        return encontrarLoteAlmacenadoEntities(true, -1, -1);
    }

    public List<LoteEgresado> encontrarLoteEgresadoEntities() {
        return encontrarLoteEgresadoEntities(true, -1, -1);
    }

    public List<LoteRemito> encontrarLoteRemitoEntities() {
        return encontrarLoteRemitoEntities(true, -1, -1);
    }

    public List<Lote> encontrarLoteEntities(int maxResults, int firstResult) {
        return encontrarLoteEntities(false, maxResults, firstResult);
    }

    public List<LoteAlmacenado> encontrarLoteAlmacenadoEntities(int maxResults, int firstResult) {
        return encontrarLoteAlmacenadoEntities(false, maxResults, firstResult);
    }

    public List<LoteEgresado> encontrarLoteEgresadoEntities(int maxResults, int firstResult) {
        return encontrarLoteEgresadoEntities(false, maxResults, firstResult);
    }

    public List<LoteRemito> encontrarLoteRemitoEntities(int maxResults, int firstResult) {
        return encontrarLoteRemitoEntities(false, maxResults, firstResult);
    }

    private List<Lote> encontrarLoteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lote.class));
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

    private List<LoteAlmacenado> encontrarLoteAlmacenadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteAlmacenado.class));
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

    private List<LoteEgresado> encontrarLoteEgresadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteEgresado.class));
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

    private List<LoteRemito> encontrarLoteRemitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LoteRemito.class));
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

    public Lote encontrarLote(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lote.class, id);
        } finally {
            em.close();
        }
    }

    public LoteAlmacenado encontrarLoteAlmacenado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteAlmacenado.class, id);
        } finally {
            em.close();
        }
    }

    public LoteEgresado encontrarLoteEgresado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteEgresado.class, id);
        } finally {
            em.close();
        }
    }

    public LoteRemito encontrarLoteRemito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteRemito.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lote> rt = cq.from(Lote.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getLoteAlmacenadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteAlmacenado> rt = cq.from(LoteAlmacenado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getLoteEgresadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteEgresado> rt = cq.from(LoteEgresado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getLoteRemitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LoteRemito> rt = cq.from(LoteRemito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Lote buscarLotePorCodigo(String codigo) throws Exception{
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Lote> c = cb.createQuery(Lote.class);
            Root<Lote> p = c.from(Lote.class);
            c.select(p).where(cb.equal(p.get("codigo"), codigo.toUpperCase()));
            Query q = em.createQuery(c);
            return (Lote) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new Exception("El código del lote ingresado no está registrado.");
        } finally {
            em.close();
        }
    }

    public LoteAlmacenado buscarLoteAlmacenado(Almacen almacen, Lote lote) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LoteAlmacenado> cq = cb.createQuery(LoteAlmacenado.class);
            Root<LoteAlmacenado> root = cq.from(LoteAlmacenado.class);
            cq.select(root);

            List<Predicate> predicateList = new ArrayList<>();

            Predicate lotePredicate, almacenPredicate;

            if (lote != null) {
                lotePredicate = cb.equal(root.get("lote"), lote);
                predicateList.add(lotePredicate);
            }

            if (almacen != null) {
                almacenPredicate = cb.equal(root.get("almacen"), almacen);
                predicateList.add(almacenPredicate);
            }
 
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);

            return (LoteAlmacenado) em.createQuery(cq).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public Boolean codigoLoteDisponible(String codigo) throws Exception {
        try {
            buscarLotePorCodigo(codigo);
            throw new Exception("El código del lote ingresado ya está registrado.");
        } catch (Exception ex) {
            return true;
        }
    }

    public Boolean codigoLoteValido(String codigo) throws Exception {
        buscarLotePorCodigo(codigo);
        return true;
    }

}
