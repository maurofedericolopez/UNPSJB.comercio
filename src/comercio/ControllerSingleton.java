package comercio;



import controladoresJPA.*;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ControllerSingleton {

    private static EntityManagerFactory emf = ComercioApp.getEntityManagerFactory();

    private static SucursalJpaController sucursalJpaController = new SucursalJpaController();
    private static AlmacenJpaController almacenJpaController = new AlmacenJpaController();
    private static PuntoVentaJpaController puntoVentaJpaController = new PuntoVentaJpaController();
    private static LoteJpaController loteJpaController = new LoteJpaController();
    private static ProductoJpaController productoJpaController = new ProductoJpaController();

    private static CategoriaJpaController categoriaJpaController = new CategoriaJpaController();
    private static MarcaJpaController marcaJpaController = new MarcaJpaController();
    private static OrigenJpaController origenJpaController = new OrigenJpaController();
    private static UnidadJpaController unidadJpaController = new UnidadJpaController();

    private static TransferenciaJpaController transferenciaJpaController = new TransferenciaJpaController();
    private static EgresoJpaController egresoJpaController = new EgresoJpaController();
    private static RemitoJpaController remitoJpaController = new RemitoJpaController();
    private static VentaJpaController ventaJpaController = new VentaJpaController();

    /**
     * @return the almacenJpaController
     */
    public static AlmacenJpaController getAlmacenJpaController() {
        return almacenJpaController;
    }

    /**
     * @return the loteJpaController
     */
    public static LoteJpaController getLoteJpaController() {
        return loteJpaController;
    }

    /**
     * @return the puntoVentaJpaController
     */
    public static PuntoVentaJpaController getPuntoVentaJpaController() {
        return puntoVentaJpaController;
    }

    /**
     * @return the categoriaJpaController
     */
    public static CategoriaJpaController getCategoriaJpaController() {
        return categoriaJpaController;
    }

    /**
     * @return the sucursalJpaController
     */
    public static SucursalJpaController getSucursalJpaController() {
        return sucursalJpaController;
    }

    /**
     * @return the marcaJpaController
     */
    public static MarcaJpaController getMarcaJpaController() {
        return marcaJpaController;
    }

    /**
     * @return the origenJpaController
     */
    public static OrigenJpaController getOrigenJpaController() {
        return origenJpaController;
    }

    /**
     * @return the unidadJpaController
     */
    public static UnidadJpaController getUnidadJpaController() {
        return unidadJpaController;
    }

    /**
     * @return the productoJpaController
     */
    public static ProductoJpaController getProductoJpaController() {
        return productoJpaController;
    }

    /**
     * @return the emf
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * @return the remitoJpaController
     */
    public static RemitoJpaController getRemitoJpaController() {
        return remitoJpaController;
    }

    /**
     * @return the egresoJpaController
     */
    public static EgresoJpaController getEgresoJpaController() {
        return egresoJpaController;
    }

    /**
     * @return the ventaJpaController
     */
    public static VentaJpaController getVentaJpaController() {
        return ventaJpaController;
    }

    /**
     * @return the transferenciaJpaController
     */
    public static TransferenciaJpaController getTransferenciaJpaController() {
        return transferenciaJpaController;
    }

}
