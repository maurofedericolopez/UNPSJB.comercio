package comercio;

import comercio.controladores.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ControllerSingleton {

    private static ProductosController productosController = new ProductosController();
    private static RemitosController remitosController = new RemitosController();
    private static LotesController lotesController = new LotesController();
    private static VentasController ventasController = new VentasController();
    private static SucursalesController sucursalesController = new SucursalesController();

    /**
     * @return the productosController
     */
    public static ProductosController getProductosController() {
        return productosController;
    }

    /**
     * @return the remitosController
     */
    public static RemitosController getRemitosController() {
        return remitosController;
    }

    /**
     * @return the lotesController
     */
    public static LotesController getLotesController() {
        return lotesController;
    }

    /**
     * @param aLotesController the lotesController to set
     */
    public static void setLotesController(LotesController aLotesController) {
        lotesController = aLotesController;
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
     * @return the sucursalesController
     */
    public static SucursalesController getSucursalesController() {
        return sucursalesController;
    }

    /**
     * @param aSucursalesController the sucursalesController to set
     */
    public static void setSucursalesController(SucursalesController aSucursalesController) {
        sucursalesController = aSucursalesController;
    }

}
