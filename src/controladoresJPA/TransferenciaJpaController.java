package controladoresJPA;

import comercio.ControllerSingleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class TransferenciaJpaController implements Serializable {

    private LoteJpaController loteJpaController;
    private AlmacenJpaController almacenJpaController;
    private PuntoVentaJpaController puntoVentaJpaController;

    public TransferenciaJpaController() {
        loteJpaController = ControllerSingleton.getLoteJpaController();
        almacenJpaController = ControllerSingleton.getAlmacenJpaController();
        puntoVentaJpaController = ControllerSingleton.getPuntoVentaJpaController();
    }

    /**
     * Transfiere productos desde un almacén hacia un punto de venta.
     * Busca un lote con el mismo código de lote.
     * Se descuenta del almacén la cantidad de productos del lote.
     * Se obtiene el producto del lote para luego aumentar el stock en venta del punto de venta especificado.
     * @param codigoLote
     * @param cantidad
     * @param almacen
     * @param puntoDeVenta
     * @throws Exception 
     */
    public void transferirProductosAVenta(String codigoLote, Double cantidad, Almacen almacen, PuntoVenta puntoDeVenta) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacen, lote, cantidad);
        Producto producto = lote.getProducto();
        puntoVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);
    }

    /**
     * Transfiere productos desde un almacén hacia otro almacén.
     * Busca un lote con el mismo código de lote.
     * Se descuenta del almacén origen de la transferencia la cantidad de productos del lote.
     * Se aumenta el stock en almacén destino de la transferencia.
     * @param codigoLote
     * @param cantidad
     * @param almacenOrigen
     * @param almacenDestino
     * @throws Exception 
     */
    public void transferirProductosAlmacen(String codigoLote, Double cantidad, Almacen almacenOrigen, Almacen almacenDestino) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacenOrigen, lote, cantidad);
        almacenJpaController.aumentarStockEnAlmacen(almacenDestino, lote, cantidad);
    }

    /**
     * Obtiene los lotes del almacén especificado que están próximos a vencer.
     * Especificamente los lotes que tienen fecha de vencimiento entre la fecha actual y dentro de 7 días.
     * @param almacen
     * @return 
     */
    public ArrayList<LoteAlmacenado> obtenerLotesProximasAVencerDeAlmacen(Almacen almacen) {
        ArrayList<LoteAlmacenado> lotesAlmacenados = new ArrayList();
        Iterator<Lote> i = loteJpaController.obtenerLotesProximosAVencer().iterator();
        while(i.hasNext()) {
            LoteAlmacenado loteAlmacenado = loteJpaController.buscarLoteAlmacenado(almacen, i.next());
            if(loteAlmacenado != null) {
                lotesAlmacenados.add(loteAlmacenado);
            }
        }
        return lotesAlmacenados;
    }

}
