package comercio;

import comercio.controladores.ProductosController;
import comercio.controladores.RemitosController;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ControllerSingleton {

    private static ProductosController productosController = new ProductosController();
    private static RemitosController remitosController = new RemitosController();

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

}
