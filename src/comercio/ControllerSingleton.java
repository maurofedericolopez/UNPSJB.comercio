package comercio;

import comercio.controladores.EgresosController;
import comercio.controladores.ImportacionesController;
import comercio.controladores.VentasController;
import comercio.controladoresJPA.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ControllerSingleton {

    private static EntityManagerFactory emf = ComercioApp.getEntityManagerFactory();
    private static EntityManager em = null;

    private static ProductoJpaController productoJpaController = new ProductoJpaController(getEmf());
    private static RemitoJpaController remitoJpaController = new RemitoJpaController(getEmf());

    private static SucursalJpaController sucursalJpaController = new SucursalJpaController(getEmf());
    private static AlmacenJpaController almacenJpaController = new AlmacenJpaController(getEmf());
    private static PuntoVentaJpaController puntoVentaJpaController = new PuntoVentaJpaController(getEmf());

    private static LoteJpaController loteJpaController = new LoteJpaController(getEmf());

    private static CategoriaJpaController categoriaJpaController = new CategoriaJpaController(getEmf());
    private static MarcaJpaController marcaJpaController = new MarcaJpaController(getEmf());
    private static OrigenJpaController origenJpaController = new OrigenJpaController(getEmf());
    private static UnidadJpaController unidadJpaController = new UnidadJpaController(getEmf());
    private static MedioDePagoJpaController medioDePagoJpaController = new MedioDePagoJpaController(getEmf());

    private static TransferenciaJpaController transferenciaJpaController = new TransferenciaJpaController(getEmf());
    private static ImportacionesController remitosController = new ImportacionesController();
    private static EgresosController egresosController = new EgresosController();
    private static VentasController ventasController = new VentasController();

    private static EgresoJpaController egresoJpaController = new EgresoJpaController(getEmf());
    private static VentaJpaController ventaJpaController = new VentaJpaController(getEmf());
    private static ItemVentaJpaController itemDeVentaJpaController = new ItemVentaJpaController(getEmf());

    /**
     * @return the remitosController
     */
    public static ImportacionesController getRemitosController() {
        return remitosController;
    }

    /**
     * @return the ventasController
     */
    public static VentasController getVentasController() {
        return ventasController;
    }

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
     * @return the medioDePagoJpaController
     */
    public static MedioDePagoJpaController getMedioDePagoJpaController() {
        return medioDePagoJpaController;
    }

    /**
     * @return the egresosController
     */
    public static EgresosController getEgresosController() {
        return egresosController;
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
     * @return the itemDeVentaJpaController
     */
    public static ItemVentaJpaController getItemDeVentaJpaController() {
        return itemDeVentaJpaController;
    }

    /**
     * @return the transferenciaJpaController
     */
    public static TransferenciaJpaController getTransferenciaJpaController() {
        return transferenciaJpaController;
    }

}
