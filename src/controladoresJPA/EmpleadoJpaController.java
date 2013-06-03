package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class EmpleadoJpaController extends Observable implements Serializable {

    private Empleado empleadoQueInicioSesion = null;

    public EmpleadoJpaController() {
        this.emf = ControllerSingleton.getEntityManagerFactory();
    }
    private EntityManagerFactory emf = null;

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private void crearEmpleado(Empleado empleado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private void editarEmpleado(Empleado empleado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            empleado = em.merge(empleado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empleado.getId();
                if (encontrarEmpleado(id) == null) {
                    throw new NonexistentEntityException("El empleado que desea actualizar en la base de datos no existe.");
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

    private void destruirEmpleado(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El empleado que desea eliminar de la base de datos no existe.", enfe);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private Empleado encontrarEmpleado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private void crearGerente(Gerente gerente) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gerente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private void editarGerente(Gerente gerente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gerente = em.merge(gerente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gerente.getId();
                if (encontrarGerente(id) == null) {
                    throw new NonexistentEntityException("El gerente que desea actualizar en la base de datos no existe.");
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

    private void destruirGerente(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gerente gerente;
            try {
                gerente = em.getReference(Gerente.class, id);
                gerente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El gerente que desea eliminar de la base de datos no existe.", enfe);
            }
            em.remove(gerente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private Gerente encontrarGerente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gerente.class, id);
        } finally {
            em.close();
        }
    }

    public int getGerenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gerente> rt = cq.from(Gerente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private void crearGestorInventario(GestorInventario gestorInventario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(gestorInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private void editarGestorInventario(GestorInventario gestorInventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            gestorInventario = em.merge(gestorInventario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = gestorInventario.getId();
                if (encontrarGestorInventario(id) == null) {
                    throw new NonexistentEntityException("El gestor de inventario que desea actualizar en la base de datos no existe.");
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

    private void destruirGestorInventario(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GestorInventario gestorInventario;
            try {
                gestorInventario = em.getReference(GestorInventario.class, id);
                gestorInventario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El gestor de inventario que desea eliminar de la base de datos no existe.", enfe);
            }
            em.remove(gestorInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private GestorInventario encontrarGestorInventario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GestorInventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getGestorInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GestorInventario> rt = cq.from(GestorInventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    private void crearVendedor(Vendedor vendedor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vendedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private void editarVendedor(Vendedor vendedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vendedor = em.merge(vendedor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = vendedor.getId();
                if (encontrarVendedor(id) == null) {
                    throw new NonexistentEntityException("El vendedor que desea actualizar en la base de datos no existe.");
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

    private void destruirVendedor(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedor vendedor;
            try {
                vendedor = em.getReference(Vendedor.class, id);
                vendedor.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El vendedor que desea eliminar de la base de datos no existe.", enfe);
            }
            em.remove(vendedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
            notificarCambios();
        }
    }

    private Vendedor encontrarVendedor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vendedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vendedor> rt = cq.from(Vendedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Gerente> obtenerTodosLosGerentes() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gerente.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Gerente> gerentes = new ArrayList();
            for(Object o : array)
                gerentes.add((Gerente) o);
            return gerentes;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<GestorInventario> obtenerTodosLosGestoresDeInventario() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GestorInventario.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<GestorInventario> gestoresDeInventario = new ArrayList();
            for(Object o : array)
                gestoresDeInventario.add((GestorInventario) o);
            return gestoresDeInventario;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    public ArrayList<Vendedor> obtenerTodosLosVendedores() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vendedor.class));
            Object[] array = em.createQuery(cq).getResultList().toArray();
            ArrayList<Vendedor> vendedores = new ArrayList();
            for(Object o : array)
                vendedores.add((Vendedor) o);
            return vendedores;
        } catch(NoResultException ex) {
            return new ArrayList();
        } finally {
            em.close();
        }
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    public void registrarNuevoGerente(String apellido, String nombre, String username, String password, Sucursal sucursal) throws Exception {
        if (usernameDisponible(username)) {
            Gerente gerente = new Gerente();
            gerente.setApellido(apellido);
            gerente.setNombre(nombre);
            gerente.setUsername(username);
            gerente.setPassword(password);
            gerente.setSucursal(sucursal);
            crearGerente(gerente);
        } else {
            throw new Exception("El nombre de usuario del gerente ya está registrado.");
        }
    }

    public void registrarNuevoGestorDeInventario(String apellido, String nombre, String username, String password, Almacen almacen) throws Exception {
        if (usernameDisponible(username)) {
            GestorInventario gestorDeInvetario = new GestorInventario();
            gestorDeInvetario.setApellido(apellido);
            gestorDeInvetario.setNombre(nombre);
            gestorDeInvetario.setUsername(username);
            gestorDeInvetario.setPassword(password);
            gestorDeInvetario.setAlmacen(almacen);
            crearGestorInventario(gestorDeInvetario);
        } else {
            throw new Exception("El nombre de usuario del gestor de inventario ya está registrado.");
        }
    }

    public void registrarNuevoVendedor(String apellido, String nombre, String username, String password, PuntoVenta puntoDeVenta) throws Exception {
        if (usernameDisponible(username)) {
            Vendedor vendedor = new Vendedor();
            vendedor.setApellido(apellido);
            vendedor.setNombre(nombre);
            vendedor.setUsername(username);
            vendedor.setPassword(password);
            vendedor.setPuntoVenta(puntoDeVenta);
            crearVendedor(vendedor);
        } else {
            throw new Exception("El nombre de usuario del vendedor ya está registrado.");
        }
    }

    private Boolean usernameDisponible(String username) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Empleado> c = cb.createQuery(Empleado.class);
            Root<Empleado> p = c.from(Empleado.class);
            c.select(p).where(cb.equal(p.get("username"), username.toUpperCase()));
            em.createQuery(c).getSingleResult();
            return false;
        } catch (NoResultException ex) {
            return true;
        } finally {
            em.close();
        }
    }

    private Empleado buscarEmpleado(String username) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Empleado> c = cb.createQuery(Empleado.class);
            Root<Empleado> p = c.from(Empleado.class);
            c.select(p).where(cb.equal(p.get("username"), username.toUpperCase()));
            return em.createQuery(c).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public Empleado iniciarSesion(String username, String password) throws Exception {
        Empleado empleado = buscarEmpleado(username);
        if(empleado != null) {
            if(empleado.getPassword().equals(password)) {
                setEmpleadoQueInicioSesion(empleado);
                return empleado;
            } else {
                throw new Exception("La contraseña es incorrecta.");
            }
        } else {
            throw new Exception("El nombre de usuario no está registrado.");
        }
    }

    public void eliminarGerente(Gerente gerente) throws NonexistentEntityException {
        destruirGerente(gerente.getId());
    }

    public void eliminarGestorDeInventario(GestorInventario gestorDeInventario) throws NonexistentEntityException {
        destruirGestorInventario(gestorDeInventario.getId());
    }

    public void eliminarVendedor(Vendedor vendedor) throws NonexistentEntityException {
        destruirVendedor(vendedor.getId());
    }

    /**
     * @return the empleadoQueInicioSesion
     */
    public Empleado getEmpleadoQueInicioSesion() {
        return empleadoQueInicioSesion;
    }

    /**
     * @param empleadoQueInicioSesion the empleadoQueInicioSesion to set
     */
    public void setEmpleadoQueInicioSesion(Empleado empleadoQueInicioSesion) {
        this.empleadoQueInicioSesion = empleadoQueInicioSesion;
    }

    public Empleado obtenerEmpleadoPorUsername(String username) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Empleado> c = cb.createQuery(Empleado.class);
            Root<Empleado> p = c.from(Empleado.class);
            c.select(p).where(cb.equal(p.get("username"), username.toUpperCase()));
            Query q = em.createQuery(c);
            return (Empleado) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new Exception("El username ingresado no está registrado.");
        } finally {
            em.close();
        }
    }

}
