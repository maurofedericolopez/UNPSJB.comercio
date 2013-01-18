package comercio.controladores;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.AlmacenJpaController;
import comercio.controladoresJPA.LoteJpaController;
import comercio.controladoresJPA.PuntoVentaJpaController;
import comercio.modelo.Almacen;
import comercio.modelo.Lote;
import comercio.modelo.Producto;
import comercio.modelo.PuntoVenta;

/**
 *
 * @author Mauro Federico Lopez
 */
public class TransferenciasController {

    private PuntoVentaJpaController puntoVentaJpaController;
    private AlmacenJpaController almacenJpaController;
    private LoteJpaController loteJpaController;

    public TransferenciasController() {
        loteJpaController = ControllerSingleton.getLoteJpaController();
        almacenJpaController = ControllerSingleton.getAlmacenJpaController();
        puntoVentaJpaController = ControllerSingleton.getPuntoVentaJpaController();
    }

    public void transferirProductosAVenta(String codigoLote, Double cantidad, Almacen almacen, PuntoVenta puntoDeVenta) throws Exception {
        Lote lote = loteJpaController.BuscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacen, codigoLote, cantidad);
        Producto producto = lote.getProducto();
        puntoVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);
    }

    public void cancelarTransferenciaAVenta() {
        
    }

    public void transferirProductosAlmacen(String codigoLote, Double cantidad, Almacen almacenOrigen, Almacen almacenDestino) throws Exception {
        Lote lote = loteJpaController.BuscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacenOrigen, codigoLote, cantidad);
        almacenJpaController.aumentarStockEnAlmacen(almacenDestino, lote, cantidad);
    }

    public void cancelarTransferenciaAlmacen() {
        
    }

}
