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
import modelo.Almacen;
import modelo.PuntoVenta;
import modelo.Sucursal;

/**
 *
 * @author Mauro Federico Lopez
 */
public class SucursalJpaController implements Serializable {

    public SucursalJpaController() {
        this.emf = ControllerSingleton.getEmf();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sucursal sucursal) {
        if (sucursal.getAlmacenes() == null) {
            sucursal.setAlmacenes(new ArrayList<Almacen>());
        }
        if (sucursal.getPuntosDeVentas() == null) {
            sucursal.setPuntosDeVentas(new ArrayList<PuntoVenta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Almacen> attachedAlmacenes = new ArrayList<Almacen>();
            for (Almacen almacenesAlmacenToAttach : sucursal.getAlmacenes()) {
                almacenesAlmacenToAttach = em.getReference(almacenesAlmacenToAttach.getClass(), almacenesAlmacenToAttach.getId());
                attachedAlmacenes.add(almacenesAlmacenToAttach);
            }
            sucursal.setAlmacenes(attachedAlmacenes);
            List<PuntoVenta> attachedPuntosDeVentas = new ArrayList<PuntoVenta>();
            for (PuntoVenta puntosDeVentasPuntoVentaToAttach : sucursal.getPuntosDeVentas()) {
                puntosDeVentasPuntoVentaToAttach = em.getReference(puntosDeVentasPuntoVentaToAttach.getClass(), puntosDeVentasPuntoVentaToAttach.getId());
                attachedPuntosDeVentas.add(puntosDeVentasPuntoVentaToAttach);
            }
            sucursal.setPuntosDeVentas(attachedPuntosDeVentas);
            em.persist(sucursal);
            for (Almacen almacenesAlmacen : sucursal.getAlmacenes()) {
                Sucursal oldSucursalOfAlmacenesAlmacen = almacenesAlmacen.getSucursal();
                almacenesAlmacen.setSucursal(sucursal);
                almacenesAlmacen = em.merge(almacenesAlmacen);
                if (oldSucursalOfAlmacenesAlmacen != null) {
                    oldSucursalOfAlmacenesAlmacen.getAlmacenes().remove(almacenesAlmacen);
                    oldSucursalOfAlmacenesAlmacen = em.merge(oldSucursalOfAlmacenesAlmacen);
                }
            }
            for (PuntoVenta puntosDeVentasPuntoVenta : sucursal.getPuntosDeVentas()) {
                Sucursal oldSucursalOfPuntosDeVentasPuntoVenta = puntosDeVentasPuntoVenta.getSucursal();
                puntosDeVentasPuntoVenta.setSucursal(sucursal);
                puntosDeVentasPuntoVenta = em.merge(puntosDeVentasPuntoVenta);
                if (oldSucursalOfPuntosDeVentasPuntoVenta != null) {
                    oldSucursalOfPuntosDeVentasPuntoVenta.getPuntosDeVentas().remove(puntosDeVentasPuntoVenta);
                    oldSucursalOfPuntosDeVentasPuntoVenta = em.merge(oldSucursalOfPuntosDeVentasPuntoVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sucursal sucursal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sucursal persistentSucursal = em.find(Sucursal.class, sucursal.getId());
            List<Almacen> almacenesOld = persistentSucursal.getAlmacenes();
            List<Almacen> almacenesNew = sucursal.getAlmacenes();
            List<PuntoVenta> puntosDeVentasOld = persistentSucursal.getPuntosDeVentas();
            List<PuntoVenta> puntosDeVentasNew = sucursal.getPuntosDeVentas();
            List<Almacen> attachedAlmacenesNew = new ArrayList<Almacen>();
            for (Almacen almacenesNewAlmacenToAttach : almacenesNew) {
                almacenesNewAlmacenToAttach = em.getReference(almacenesNewAlmacenToAttach.getClass(), almacenesNewAlmacenToAttach.getId());
                attachedAlmacenesNew.add(almacenesNewAlmacenToAttach);
            }
            almacenesNew = attachedAlmacenesNew;
            sucursal.setAlmacenes(almacenesNew);
            List<PuntoVenta> attachedPuntosDeVentasNew = new ArrayList<PuntoVenta>();
            for (PuntoVenta puntosDeVentasNewPuntoVentaToAttach : puntosDeVentasNew) {
                puntosDeVentasNewPuntoVentaToAttach = em.getReference(puntosDeVentasNewPuntoVentaToAttach.getClass(), puntosDeVentasNewPuntoVentaToAttach.getId());
                attachedPuntosDeVentasNew.add(puntosDeVentasNewPuntoVentaToAttach);
            }
            puntosDeVentasNew = attachedPuntosDeVentasNew;
            sucursal.setPuntosDeVentas(puntosDeVentasNew);
            sucursal = em.merge(sucursal);
            for (Almacen almacenesOldAlmacen : almacenesOld) {
                if (!almacenesNew.contains(almacenesOldAlmacen)) {
                    almacenesOldAlmacen.setSucursal(null);
                    almacenesOldAlmacen = em.merge(almacenesOldAlmacen);
                }
            }
            for (Almacen almacenesNewAlmacen : almacenesNew) {
                if (!almacenesOld.contains(almacenesNewAlmacen)) {
                    Sucursal oldSucursalOfAlmacenesNewAlmacen = almacenesNewAlmacen.getSucursal();
                    almacenesNewAlmacen.setSucursal(sucursal);
                    almacenesNewAlmacen = em.merge(almacenesNewAlmacen);
                    if (oldSucursalOfAlmacenesNewAlmacen != null && !oldSucursalOfAlmacenesNewAlmacen.equals(sucursal)) {
                        oldSucursalOfAlmacenesNewAlmacen.getAlmacenes().remove(almacenesNewAlmacen);
                        oldSucursalOfAlmacenesNewAlmacen = em.merge(oldSucursalOfAlmacenesNewAlmacen);
                    }
                }
            }
            for (PuntoVenta puntosDeVentasOldPuntoVenta : puntosDeVentasOld) {
                if (!puntosDeVentasNew.contains(puntosDeVentasOldPuntoVenta)) {
                    puntosDeVentasOldPuntoVenta.setSucursal(null);
                    puntosDeVentasOldPuntoVenta = em.merge(puntosDeVentasOldPuntoVenta);
                }
            }
            for (PuntoVenta puntosDeVentasNewPuntoVenta : puntosDeVentasNew) {
                if (!puntosDeVentasOld.contains(puntosDeVentasNewPuntoVenta)) {
                    Sucursal oldSucursalOfPuntosDeVentasNewPuntoVenta = puntosDeVentasNewPuntoVenta.getSucursal();
                    puntosDeVentasNewPuntoVenta.setSucursal(sucursal);
                    puntosDeVentasNewPuntoVenta = em.merge(puntosDeVentasNewPuntoVenta);
                    if (oldSucursalOfPuntosDeVentasNewPuntoVenta != null && !oldSucursalOfPuntosDeVentasNewPuntoVenta.equals(sucursal)) {
                        oldSucursalOfPuntosDeVentasNewPuntoVenta.getPuntosDeVentas().remove(puntosDeVentasNewPuntoVenta);
                        oldSucursalOfPuntosDeVentasNewPuntoVenta = em.merge(oldSucursalOfPuntosDeVentasNewPuntoVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = sucursal.getId();
                if (findSucursal(id) == null) {
                    throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.");
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
            Sucursal sucursal;
            try {
                sucursal = em.getReference(Sucursal.class, id);
                sucursal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sucursal with id " + id + " no longer exists.", enfe);
            }
            List<Almacen> almacenes = sucursal.getAlmacenes();
            for (Almacen almacenesAlmacen : almacenes) {
                almacenesAlmacen.setSucursal(null);
                almacenesAlmacen = em.merge(almacenesAlmacen);
            }
            List<PuntoVenta> puntosDeVentas = sucursal.getPuntosDeVentas();
            for (PuntoVenta puntosDeVentasPuntoVenta : puntosDeVentas) {
                puntosDeVentasPuntoVenta.setSucursal(null);
                puntosDeVentasPuntoVenta = em.merge(puntosDeVentasPuntoVenta);
            }
            em.remove(sucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sucursal> findSucursalEntities() {
        return findSucursalEntities(true, -1, -1);
    }

    public List<Sucursal> findSucursalEntities(int maxResults, int firstResult) {
        return findSucursalEntities(false, maxResults, firstResult);
    }

    private List<Sucursal> findSucursalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sucursal.class));
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

    public Sucursal findSucursal(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sucursal.class, id);
        } finally {
            em.close();
        }
    }

    public int getSucursalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sucursal> rt = cq.from(Sucursal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Sucursal> obtenerTodasLasSucursales() {
        ArrayList<Sucursal> sucursales = new ArrayList();
        Object[] array = findSucursalEntities().toArray();
        for(Object o : array)
            sucursales.add((Sucursal) o);
        return sucursales;
    }

}
