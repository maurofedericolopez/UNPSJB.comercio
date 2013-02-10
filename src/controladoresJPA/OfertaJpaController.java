package controladoresJPA;

import comercio.ControllerSingleton;
import controladoresJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.JOptionPane;
import modelo.Categoria;
import modelo.Marca;
import modelo.Oferta;
import modelo.Producto;

/**
 *
 * @author Mauro Federico Lopez
 */
public class OfertaJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    private ProductoJpaController productoJpaController;

    public OfertaJpaController() {
        this.emf = ControllerSingleton.getEmf();
        productoJpaController = ControllerSingleton.getProductoJpaController();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crearOferta(Oferta oferta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(oferta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editarOferta(Oferta oferta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            oferta = em.merge(oferta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = oferta.getId();
                if (encontrarOferta(id) == null) {
                    throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
            productoJpaController.notificarCambios();
        }
    }

    public void destruirOferta(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Oferta oferta;
            try {
                oferta = em.getReference(Oferta.class, id);
                oferta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The oferta with id " + id + " no longer exists.", enfe);
            }
            em.remove(oferta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Oferta> encontrarOfertaEntities() {
        return encontrarOfertaEntities(true, -1, -1);
    }

    public List<Oferta> encontrarOfertaEntities(int maxResults, int firstResult) {
        return encontrarOfertaEntities(false, maxResults, firstResult);
    }

    private List<Oferta> encontrarOfertaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Oferta.class));
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

    public Oferta encontrarOferta(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Oferta.class, id);
        } finally {
            em.close();
        }
    }

    public int getOfertaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Oferta> rt = cq.from(Oferta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void crearOfertaParaProducto(String codigoProducto, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        Producto producto = productoJpaController.buscarProductoPorCodigo(codigoProducto);
        crearOfertaParaProducto(producto, descuento, fechaInicio, fechaFin);
    }

    private void crearOfertaParaProducto(Producto producto, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        if(descuento < 0.0) {
            descuento = 0.0;
        } else
            if(descuento > 1) {
                descuento = 1.0;
            }
        Oferta oferta = producto.getOferta();
        if(oferta != null) {
            if (oferta.getFechaFin().after(new Date())) {
                String msg = "El producto " + producto.getDescripcion() + " tiene una oferta asociada en vigencia."
                        + "\nÉsta vence el " + oferta.getFechaFin().toLocaleString()
                        + "\n¿Desea reemplazar la oferta?";
                int showOptionDialog = JOptionPane.showOptionDialog(null, msg, "Oferta", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (showOptionDialog == 0) {
                    oferta.setDescuento(descuento);
                    oferta.setFechaFin(fechaFin);
                    oferta.setFechaInicio(fechaInicio);
                    editarOferta(oferta);
                }
            } else {
                oferta.setDescuento(descuento);
                oferta.setFechaFin(fechaFin);
                oferta.setFechaInicio(fechaInicio);
                editarOferta(oferta);
            }
        } else {
            oferta = new Oferta();
            oferta.setDescuento(descuento);
            oferta.setFechaFin(fechaFin);
            oferta.setFechaInicio(fechaInicio);
            crearOferta(oferta);
            producto.setOferta(oferta);
            productoJpaController.editarProducto(producto);
        }
    }

    public void crearOfertaParaProductosDeUnaCategoria(Categoria categoria, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        ArrayList<Producto> productos = productoJpaController.obtenerProductosPorCategoria(categoria);
        Iterator<Producto> i = productos.iterator();
        while(i.hasNext())
            crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
    }

    public void crearOfertaParaProductosDeUnaMarca(Marca marca, Double descuento, Date fechaInicio, Date fechaFin) throws Exception {
        ArrayList<Producto> productos = productoJpaController.obtenerProductosPorMarca(marca);
        Iterator<Producto> i = productos.iterator();
        while(i.hasNext())
            crearOfertaParaProducto(i.next(), descuento, fechaInicio, fechaFin);
    }

}
