package controladoresJPA;

import comercio.ControllerSingleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import modelo.*;

/**
 * Ésta clase se encarga de realizar las transferencias de productos hacia un punto de venta o almacén.
 * @author Mauro Federico Lopez
 */
public class TransferenciaJpaController implements Serializable {

    private LoteJpaController loteJpaController;
    private AlmacenJpaController almacenJpaController;
    private PuntoVentaJpaController puntoVentaJpaController;

    /**
     * Construye un nuevo controlador para realizar las transferencias de productos.
     */
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
     * @param codigoLote es el codigo del lote a transferir.
     * @param cantidad es la cantidad que requiere transferir.
     * @param almacen es el <code>Almacen</code> de donde se transferirán los productos.
     * @param puntoDeVenta es el <code>PuntoVenta</code> donde se transferirán los productos.
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
     * @param codigoLote es el codigo del lote a transferir.
     * @param cantidad es la cantidad que requiere transferir.
     * @param almacenOrigen es el <code>Almacen</code> de donde se transferirán los productos.
     * @param almacenDestino es el <code>Almacen</code> donde se transferirán los productos.
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
     * @param almacen es el <code>Almacen</code> de donde se obtendrán los lotes próximos a vencer.
     * @return lotesAlmacenados
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
