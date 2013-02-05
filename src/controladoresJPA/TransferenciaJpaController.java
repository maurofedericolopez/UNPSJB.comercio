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

    public void transferirProductosAVenta(String codigoLote, Double cantidad, Almacen almacen, PuntoVenta puntoDeVenta) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacen, lote, cantidad);
        Producto producto = lote.getProducto();
        puntoVentaJpaController.aumentarStockEnVenta(puntoDeVenta, producto, cantidad);
    }

    public void transferirProductosAlmacen(String codigoLote, Double cantidad, Almacen almacenOrigen, Almacen almacenDestino) throws Exception {
        Lote lote = loteJpaController.buscarLotePorCodigo(codigoLote);
        almacenJpaController.descontarDeAlmacen(almacenOrigen, lote, cantidad);
        almacenJpaController.aumentarStockEnAlmacen(almacenDestino, lote, cantidad);
    }

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
