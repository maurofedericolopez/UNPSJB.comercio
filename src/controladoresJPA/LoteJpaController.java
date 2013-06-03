package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 * Ésta clase se encarga de las operaciones CRUD de laa entidades <code>Lote</code>, <code>LoteAlmacenado</code>, <code>LoteEgresado</code> y <code>LoteRemito</code>.
 * @author Mauro Federico Lopez
 */
public class LoteJpaController implements Serializable {

    /**
     * Construye un nuevo controlador para las entidades <code>Lote</code>, <code>LoteAlmacenado</code>, <code>LoteEgresado</code> y <code>LoteRemito</code>.
     */
    public LoteJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Lote</code> en la base de datos.
     * @param lote es el <code>Lote</code> que se persistirá.
     */
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

    /**
     * Persiste un objeto <code>LoteAlmacenado</code> en la base de datos.
     * @param loteAlmacenado es el <code>LoteAlmacenado</code> que se persistirá.
     */
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

    /**
     * Persiste un objeto <code>LoteEgresado</code> en la base de datos.
     * @param loteEgresado es el <code>LoteEgresado</code> que se persistirá.
     */
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

    /**
     * Persiste un objeto <code>LoteRemito</code> en la base de datos.
     * @param loteRemito es el <code>LoteRemito</code> que se persistirá.
     */
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
                remito.getLotesDelRemito().add(loteRemito);
                remito = em.merge(remito);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza un objeto <code>Lote</code> en la base de datos.
     * @param lote es el <code>Lote</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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

    /**
     * Actualiza un objeto <code>LoteAlmacenado</code> en la base de datos.
     * @param loteAlmacenado es el <code>LoteAlmacenado</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote almacenado que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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

    /**
     * Actualiza un objeto <code>LoteEgresado</code> en la base de datos.
     * @param loteEgresado es el <code>LoteEgresado</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote egresado que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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

    /**
     * Actualiza un objeto <code>LoteRemito</code> en la base de datos.
     * @param loteRemito es el <code>LoteRemito</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote remito que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
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
                remitoOld.getLotesDelRemito().remove(loteRemito);
                remitoOld = em.merge(remitoOld);
            }
            if (remitoNew != null && !remitoNew.equals(remitoOld)) {
                remitoNew.getLotesDelRemito().add(loteRemito);
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

    /**
     * Elimina un lote de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del lote en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote que se quiere eliminar no existe en la base de datos.
     */
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

    /**
     * Elimina un lote almacenado de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del lote almacenado en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote almacenado que se quiere eliminar no existe en la base de datos.
     */
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

    /**
     * Elimina un lote egresado de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del lote egresado en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote egresado que se quiere eliminar no existe en la base de datos.
     */
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

    /**
     * Elimina un lote remito de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del lote remito en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el lote remito que se quiere eliminar no existe en la base de datos.
     */
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
                remito.getLotesDelRemito().remove(loteRemito);
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

    /**
     * Devuelve un objeto <code>Lote</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del lote en la base de datos.
     * @return 
     */
    public Lote encontrarLote(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lote.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>LoteAlmacenado</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del lote almacenado en la base de datos.
     * @return 
     */
    public LoteAlmacenado encontrarLoteAlmacenado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteAlmacenado.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>LoteEgresado</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del lote egresado en la base de datos.
     * @return 
     */
    public LoteEgresado encontrarLoteEgresado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteEgresado.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve un objeto <code>LoteRemito</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del lote remito en la base de datos.
     * @return 
     */
    public LoteRemito encontrarLoteRemito(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LoteRemito.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de lotes registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de lotes almacenados registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de lotes egresadosregistrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve la cantidad de lotes remitos registrados en la base de datos.
     * @return cantidad
     */
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

    /**
     * Devuelve un objeto <code>Lote</code> buscado por su codigo en la base de datos.
     * @param codigo es el codigo del lote qu se buscará.
     * @return lote
     * @throws Exception Se lanza si el codigo del lote no está registrado.
     */
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

    /**
     * Devuelve un objeto <code>LoteAlmacenado</code> buscado por almacen y lote en la base de datos.
     * @param almacen es el <code>Almacen</code> por el cual se buscará.
     * @param lote es el <code>Lote</code> por el cual se buscará.
     * @return loteAlmacenado
     */
    public LoteAlmacenado buscarLoteAlmacenado(Almacen almacen, Lote lote) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<LoteAlmacenado> cq = cb.createQuery(LoteAlmacenado.class);
            Root<LoteAlmacenado> root = cq.from(LoteAlmacenado.class);
            cq.select(root);

            List<Predicate> predicateList = new ArrayList<Predicate>();

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

    /**
     * Devolverá <code>true</code> si el codigo del lote no fue registrado en la base de datos.
     * @param codigo es el codigo que se analizará.
     * @return boolean
     * @throws Exception se lanza si el codigo del lote ya está registrado en la base de datos.
     */
    public Boolean codigoLoteDisponible(String codigo) throws Exception {
        try {
            buscarLotePorCodigo(codigo);
            throw new Exception("El código del lote ingresado ya está registrado.");
        } catch (Exception ex) {
            return true;
        }
    }

    /**
     * Devuelve una lista con todos los lotes que estan próximos a vencer en el periodo de una semana.
     * @return lotesProximosAVencer
     */
    public ArrayList<Lote> obtenerLotesProximosAVencer() {
        Date hoy = new Date();
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoy);
        calendar.add(Calendar.DATE, 7);
        Date unaSemanaDespues = calendar.getTime();

        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Lote> cq = cb.createQuery(Lote.class);
            Root<Lote> root = cq.from(Lote.class);
            cq.select(root);
            cq.where(cb.between(root.get("fechaVencimiento").as(Date.class), hoy, unaSemanaDespues));
            ArrayList<Lote> lotes = new ArrayList();
            Object[] array = em.createQuery(cq).getResultList().toArray();
            for(Object o : array)
                lotes.add((Lote) o);
            return lotes;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de productos que hay en un lote almacenado por un almacen.
     * @param almacen es el <code>Almacen</code> de donde se buscará el lote.
     * @param codigoLote es el codigo del lote que se buscará.
     * @return cantidad
     * @throws Exception 
     */
    public Double cantidadDeProductosEnLote(Almacen almacen, String codigoLote) throws Exception {
        Lote lote = buscarLotePorCodigo(codigoLote);
        LoteAlmacenado loteAlmacenado = buscarLoteAlmacenado(almacen, lote);
        if(loteAlmacenado != null)
            return loteAlmacenado.getCantidad();
        else
            throw new Exception("El almacen no contiene este lote.");
    }

    /**
     * Devuelve una lista con todos los lotes que contienen al producto especificado.
     * @param producto es el <code>Producto</code> que se buscará en los lotes.
     * @return lotes
     */
    public ArrayList<Lote> obtenerLotesPorProducto(Producto producto) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Lote> cq = cb.createQuery(Lote.class);
            Root<Lote> root = cq.from(Lote.class);
            cq.select(root);
            cq.where(cb.equal(root.get("producto"), producto));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Lote> lotes = new ArrayList();
            for(Object o : array)
                lotes.add((Lote) o);
            return lotes;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

}
