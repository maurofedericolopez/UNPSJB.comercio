package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Unidad;

/**
 * Ésta clase se encarga de las operaciones CRUD de la entidad <code>Unidad</code>.
 * @author Mauro Federico Lopez
 */
public class UnidadJpaController implements Serializable {

    /**
     * Construye un nuevo controlador para la entidad <code>Unidad</code>.
     */
    public UnidadJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Unidad</code> en la base de datos.
     * @param unidad es la <code>Unidad</code> que se persistirá.
     */
    public void crearUnidad(Unidad unidad) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(unidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza un objeto <code>Unidad</code> en la base de datos.
     * @param unidad es la <code>Unidad</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la unidad que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarUnidad(Unidad unidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            unidad = em.merge(unidad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = unidad.getId();
                if (encontrarUnidad(id) == null) {
                    throw new NonexistentEntityException("The unidad with id " + id + " no longer exists.");
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
     * Elimina una unidad de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> de la unidad en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la unidad que se quiere eliminar no existe en la base de datos.
     */
    public void destruirUnidad(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unidad unidad;
            try {
                unidad = em.getReference(Unidad.class, id);
                unidad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unidad with id " + id + " no longer exists.", enfe);
            }
            em.remove(unidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Devuelve un objeto <code>Unidad</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> de la unidad en la base de datos.
     * @return 
     */
    public Unidad encontrarUnidad(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Unidad.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de unidades registradas en la base de datos.
     * @return cantidad
     */
    public int getUnidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Unidad> rt = cq.from(Unidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista de todas las unidades registradas en la base de datos.
     * Devuelve un ArrayList de unidades.
     * @return unidades
     */
    public ArrayList<Unidad> obtenerTodasLasUnidades() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Unidad.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Unidad> unidades = new ArrayList();
            for(Object o : array)
                unidades.add((Unidad) o);
            return unidades;
        } catch (NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

}
