package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Categoria;

/**
 * Ésta clase se encarga de las operaciones CRUD de la entidad <code>Categoria</code>.
 * @author Mauro Federico Lopez
 */
public class CategoriaJpaController extends Observable implements Serializable {

    /**
     * Construye un nuevo controlador para la entidad <code>Categoria</code>.
     */
    public CategoriaJpaController() {
        this.emf = ControllerSingleton.getEmf();
    }

    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Persiste un objeto <code>Categoria</code> en la base de datos.
     * @param categoria es la <code>Categoria</code> que se persistirá.
     */
    public void crearCategoria(Categoria categoria) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    /**
     * Actualiza un objeto <code>Categoria</code> en la base de datos.
     * @param categoria es la <code>Categoria</code> que se actualizará en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la categoria que se quiere actualizar no existe en la base de datos.
     * @throws Exception 
     */
    public void editarCategoria(Categoria categoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            categoria = em.merge(categoria);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = categoria.getId();
                if (encontrarCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    /**
     * Elimina una categoria de la base de datos con el <code>id</code> especificado.
     * @param id el <code>id</code> de la categoría en la base de datos.
     * @throws NonexistentEntityException Se lanza ésta excepción cuando la categoria que se quiere eliminar no existe en la base de datos.
     */
    public void destruirCategoria(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }
    private List<Categoria> encontrarCategoriaEntities() {
        return encontrarCategoriaEntities(true, -1, -1);
    }

    private List<Categoria> encontrarCategoriaEntities(int maxResults, int firstResult) {
        return encontrarCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> encontrarCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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
     * Devuelve un objeto <code>Categoria</code> buscado por su id en la base de datos.
     * @param id el <code>id</code> de la categoría en la base de datos.
     * @return categoria.
     */
    public Categoria encontrarCategoria(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve la cantidad de categorias registrados en la base de datos.
     * @return cantidad
     */
    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Devuelve una lista de todas las categorias registradas en la base de datos.
     * Devuelve un ArrayList de categorias.
     * @return categorias
     */
    public ArrayList<Categoria> obtenerTodasLasCategorias() {
        ArrayList<Categoria> categorias = new ArrayList();
        Object[] array = encontrarCategoriaEntities().toArray();
        for(Object o : array)
            categorias.add((Categoria) o);
        return categorias;
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

}
