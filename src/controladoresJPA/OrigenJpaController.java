package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Origen;

/**
 * Ésta clase se encarga de las operaciones CRUD de la entidad <code>Origen</code>.
 * @author Mauro Federico Lopez
 */
public class OrigenJpaController implements Serializable {

    /**
     * Construye un nuevo controlador para la entidad <code>Origen</code>.
     */
    public OrigenJpaController() {
        this.emf = ControllerSingleton.getEmf();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Origen</code> en la base de datos.
     * @param origen es la <code>Origen</code> que se persistirá.
     */
    public void crearOrigen(Origen origen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(origen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Actualiza un objeto <code>Origen</code> en la base de datos.
     * @param origen es la <code>Origen</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el origen que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarOrigen(Origen origen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            origen = em.merge(origen);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = origen.getId();
                if (encontrarOrigen(id) == null) {
                    throw new NonexistentEntityException("The origen with id " + id + " no longer exists.");
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
     * Elimina un origen de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> del origen en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando el origen que se quiere eliminar no existe en la base de datos.
     */
    public void destruirOrigen(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Origen origen;
            try {
                origen = em.getReference(Origen.class, id);
                origen.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The origen with id " + id + " no longer exists.", enfe);
            }
            em.remove(origen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private List<Origen> encontrarOrigenEntities() {
        return encontrarOrigenEntities(true, -1, -1);
    }

    private List<Origen> encontrarOrigenEntities(int maxResults, int firstResult) {
        return encontrarOrigenEntities(false, maxResults, firstResult);
    }

    private List<Origen> encontrarOrigenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Origen.class));
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

    /**
     * Devuelve un objeto <code>Origen</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> del origen en la base de datos.
     * @return origen
     */
    public Origen encontrarOrigen(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Origen.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de origenes registrados en la base de datos.
     * @return cantidad
     */
    public int getOrigenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Origen> rt = cq.from(Origen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista de todos los origenes registrados en la base de datos.
     * Devuelve un ArrayList de origenes.
     * @return origenes
     */
    public ArrayList<Origen> obtenerTodosLosOrigen() {
        ArrayList<Origen> origenes = new ArrayList();
        Object[] array = encontrarOrigenEntities().toArray();
        for(Object o : array)
            origenes.add((Origen) o);
        return origenes;
    }

}
