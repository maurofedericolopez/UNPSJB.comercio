package comercio;

import comercio.controladores.*;
import comercio.controladoresJPA.*;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ControllerSingleton {

    private static EntityManagerFactory emf = ComercioApp.getEntityManager();
    private static ProductosController productosController = new ProductosController();
    private static ProductoJpaController productoJpaController = new ProductoJpaController(getEmf());
    private static ProductoEnVentaJpaController productoEnVentaJpaController = new ProductoEnVentaJpaController(getEmf());
    private static ImportacionesController remitosController = new ImportacionesController();
    private static RemitoJpaController remitoJpaController = new RemitoJpaController(getEmf());
    private static SucursalJpaController sucursalJpaController = new SucursalJpaController(getEmf());
    private static AlmacenJpaController almacenJpaController = new AlmacenJpaController(getEmf());
    private static PuntoVentaJpaController puntoVentaJpaController = new PuntoVentaJpaController(getEmf());
    private static LoteJpaController loteJpaController = new LoteJpaController(getEmf());
    private static LoteAlmacenadoJpaController loteAlmacenadoJpaController = new LoteAlmacenadoJpaController(getEmf());
    private static LoteRemitoJpaController loteRemitoJpaController = new LoteRemitoJpaController(getEmf());
    private static LoteEgresadoJpaController loteEgresadoJpaController = new LoteEgresadoJpaController(getEmf());

    private static CategoriaJpaController categoriaJpaController = new CategoriaJpaController(getEmf());
    private static MarcaJpaController marcaJpaController = new MarcaJpaController(getEmf());
    private static OrigenJpaController origenJpaController = new OrigenJpaController(getEmf());
    private static UnidadJpaController unidadJpaController = new UnidadJpaController(getEmf());

    private static MedioDePagoJpaController medioDePagoJpaController = new MedioDePagoJpaController(getEmf());

    private static TransferenciasController transferenciasController = new TransferenciasController();
    private static EgresosController egresosController = new EgresosController();
    private static VentasController ventasController = new VentasController();

    /**
     * @return the productosController
     */
    public static ProductosController getProductosController() {
        return productosController;
    }

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
     * @param aVentasController the ventasController to set
     */
    public static void setVentasController(VentasController aVentasController) {
        ventasController = aVentasController;
    }

    /**
     * @return the almacenJpaController
     */
    public static AlmacenJpaController getAlmacenJpaController() {
        return almacenJpaController;
    }

    /**
     * @param aAlmacenJpaController the almacenJpaController to set
     */
    public static void setAlmacenJpaController(AlmacenJpaController aAlmacenJpaController) {
        almacenJpaController = aAlmacenJpaController;
    }

    /**
     * @return the loteJpaController
     */
    public static LoteJpaController getLoteJpaController() {
        return loteJpaController;
    }

    /**
     * @param aLoteJpaController the loteJpaController to set
     */
    public static void setLoteJpaController(LoteJpaController aLoteJpaController) {
        loteJpaController = aLoteJpaController;
    }

    /**
     * @return the puntoVentaJpaController
     */
    public static PuntoVentaJpaController getPuntoVentaJpaController() {
        return puntoVentaJpaController;
    }

    /**
     * @param aPuntoVentaJpaController the puntoVentaJpaController to set
     */
    public static void setPuntoVentaJpaController(PuntoVentaJpaController aPuntoVentaJpaController) {
        puntoVentaJpaController = aPuntoVentaJpaController;
    }

    /**
     * @return the categoriaJpaController
     */
    public static CategoriaJpaController getCategoriaJpaController() {
        return categoriaJpaController;
    }

    /**
     * @param aCategoriaJpaController the categoriaJpaController to set
     */
    public static void setCategoriaJpaController(CategoriaJpaController aCategoriaJpaController) {
        categoriaJpaController = aCategoriaJpaController;
    }

    /**
     * @return the sucursalJpaController
     */
    public static SucursalJpaController getSucursalJpaController() {
        return sucursalJpaController;
    }

    /**
     * @param aSucursalJpaController the sucursalJpaController to set
     */
    public static void setSucursalJpaController(SucursalJpaController aSucursalJpaController) {
        sucursalJpaController = aSucursalJpaController;
    }

    /**
     * @return the marcaJpaController
     */
    public static MarcaJpaController getMarcaJpaController() {
        return marcaJpaController;
    }

    /**
     * @param aMarcaJpaController the marcaJpaController to set
     */
    public static void setMarcaJpaController(MarcaJpaController aMarcaJpaController) {
        marcaJpaController = aMarcaJpaController;
    }

    /**
     * @return the origenJpaController
     */
    public static OrigenJpaController getOrigenJpaController() {
        return origenJpaController;
    }

    /**
     * @param aOrigenJpaController the origenJpaController to set
     */
    public static void setOrigenJpaController(OrigenJpaController aOrigenJpaController) {
        origenJpaController = aOrigenJpaController;
    }

    /**
     * @return the unidadJpaController
     */
    public static UnidadJpaController getUnidadJpaController() {
        return unidadJpaController;
    }

    /**
     * @param aUnidadJpaController the unidadJpaController to set
     */
    public static void setUnidadJpaController(UnidadJpaController aUnidadJpaController) {
        unidadJpaController = aUnidadJpaController;
    }

    /**
     * @return the medioDePagoJpaController
     */
    public static MedioDePagoJpaController getMedioDePagoJpaController() {
        return medioDePagoJpaController;
    }

    /**
     * @param aMedioDePagoJpaController the medioDePagoJpaController to set
     */
    public static void setMedioDePagoJpaController(MedioDePagoJpaController aMedioDePagoJpaController) {
        medioDePagoJpaController = aMedioDePagoJpaController;
    }

    /**
     * @return the transferenciasController
     */
    public static TransferenciasController getTransferenciasController() {
        return transferenciasController;
    }

    /**
     * @param aTransferenciasController the transferenciasController to set
     */
    public static void setTransferenciasController(TransferenciasController aTransferenciasController) {
        transferenciasController = aTransferenciasController;
    }

    /**
     * @return the egresosController
     */
    public static EgresosController getEgresosController() {
        return egresosController;
    }

    /**
     * @param aEgresosController the egresosController to set
     */
    public static void setEgresosController(EgresosController aEgresosController) {
        egresosController = aEgresosController;
    }

    /**
     * @return the loteAlmacenadoJpaController
     */
    public static LoteAlmacenadoJpaController getLoteAlmacenadoJpaController() {
        return loteAlmacenadoJpaController;
    }

    /**
     * @param aLoteAlmacenadoJpaController the loteAlmacenadoJpaController to set
     */
    public static void setLoteAlmacenadoJpaController(LoteAlmacenadoJpaController aLoteAlmacenadoJpaController) {
        loteAlmacenadoJpaController = aLoteAlmacenadoJpaController;
    }

    /**
     * @return the loteRemitoJpaController
     */
    public static LoteRemitoJpaController getLoteRemitoJpaController() {
        return loteRemitoJpaController;
    }

    /**
     * @param aLoteRemitoJpaController the loteRemitoJpaController to set
     */
    public static void setLoteRemitoJpaController(LoteRemitoJpaController aLoteRemitoJpaController) {
        loteRemitoJpaController = aLoteRemitoJpaController;
    }

    /**
     * @return the loteEgresadoJpaController
     */
    public static LoteEgresadoJpaController getLoteEgresadoJpaController() {
        return loteEgresadoJpaController;
    }

    /**
     * @param aLoteEgresadoJpaController the loteEgresadoJpaController to set
     */
    public static void setLoteEgresadoJpaController(LoteEgresadoJpaController aLoteEgresadoJpaController) {
        loteEgresadoJpaController = aLoteEgresadoJpaController;
    }

    /**
     * @return the productoEnVentaJpaController
     */
    public static ProductoEnVentaJpaController getProductoEnVentaJpaController() {
        return productoEnVentaJpaController;
    }

    /**
     * @param aProductoEnVentaJpaController the productoEnVentaJpaController to set
     */
    public static void setProductoEnVentaJpaController(ProductoEnVentaJpaController aProductoEnVentaJpaController) {
        productoEnVentaJpaController = aProductoEnVentaJpaController;
    }

    /**
     * @return the productoJpaController
     */
    public static ProductoJpaController getProductoJpaController() {
        return productoJpaController;
    }

    /**
     * @param aProductoJpaController the productoJpaController to set
     */
    public static void setProductoJpaController(ProductoJpaController aProductoJpaController) {
        productoJpaController = aProductoJpaController;
    }

    /**
     * @return the emf
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * @param aEmf the emf to set
     */
    public static void setEmf(EntityManagerFactory aEmf) {
        emf = aEmf;
    }

    /**
     * @return the remitoJpaController
     */
    public static RemitoJpaController getRemitoJpaController() {
        return remitoJpaController;
    }

    /**
     * @param aRemitoJpaController the remitoJpaController to set
     */
    public static void setRemitoJpaController(RemitoJpaController aRemitoJpaController) {
        remitoJpaController = aRemitoJpaController;
    }

}
