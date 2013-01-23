package comercio.controladores;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.LoteJpaController;
import comercio.controladoresJPA.ProductoJpaController;
import comercio.controladoresJPA.RemitoJpaController;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ImportacionesController extends Observable {

    private RemitoJpaController remitoJpaController;
    private LoteJpaController loteJpaController;
    private ProductoJpaController productoJpaController;

    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();
    private Remito remito = null;
    private Almacen almacen = null;

    public ImportacionesController() {
        super();
        loteJpaController = ControllerSingleton.getLoteJpaController();
        productoJpaController = ControllerSingleton.getProductoJpaController();
        remitoJpaController = ControllerSingleton.getRemitoJpaController();
    }

    public void agregarLote(String codigoLote, String codigoProducto, Date fechaProduccion, Date fechaVencimiento, Double cantidad) throws Exception {
        if (loteJpaController.codigoLoteDisponible(codigoLote) && codigoNoSeAgrego(codigoLote)) {
            Producto producto = productoJpaController.buscarProductoPorCodigo(codigoLote);

            Lote lote = new Lote();
            lote.setCodigo(codigoLote);
            lote.setProducto(producto);
            lote.setFechaProduccion(fechaProduccion);
            lote.setFechaVencimiento(fechaVencimiento);

            LoteRemito loteRemito = new LoteRemito();
            loteRemito.setLote(lote);
            loteRemito.setRemito(null);
            loteRemito.setCantidadIngresada(cantidad);
            getLotesDelRemito().add(loteRemito);

            notificarCambios();
        }
    }

    public void eliminarLote(LoteRemito loteRemito) {
        getLotesDelRemito().remove(loteRemito);
    }

    public void registrarDatosRemito(Remito remito, Almacen almacen) throws Exception {
        if (almacen != null) {
            if (remito != null) {
                this.remito = remito;
                this.almacen = almacen;
                notificarCambios();
            } else {
                throw new Exception("No ha ingresado la fecha");
            }
        } else {
            throw new Exception("No ha seleccionado ningún almacén");
        }
    }

    public void persistirOperacion() {
        remitoJpaController.create(remito);
        Iterator<LoteRemito> i = getLotesDelRemito().iterator();
        while(i.hasNext()) {
            LoteRemito loteRemito = i.next();

            Lote lote = loteRemito.getLote();
            loteJpaController.crearLote(lote);

            loteRemito.setRemito(remito);
            loteJpaController.crearLoteRemito(loteRemito);

            Double cantidad = loteRemito.getCantidadIngresada();
            LoteAlmacenado loteAlmacenado = new LoteAlmacenado();
            loteAlmacenado.setAlmacen(almacen);
            loteAlmacenado.setLote(lote);
            loteAlmacenado.setCantidad(cantidad);
            loteJpaController.crearLoteAlmacenado(loteAlmacenado);
        }
    }

    public void cancelarIngresoDeLotesDeProductos() {
        limpiarVariables();
    }

    private Boolean codigoNoSeAgrego(String codigo) {
        Iterator<LoteRemito> i = getLotesDelRemito().iterator();
        while(i.hasNext())
            if(i.next().getLote().getCodigo().equals(codigo))
                return false;
        return true;
    }

    private void limpiarVariables() {
        remito = null;
        almacen = null;
        lotesDelRemito = new ArrayList();
    }

    private void notificarCambios() {
        setChanged();
        notifyObservers();
    }

    /**
     * @return the lotesDelRemito
     */
    public ArrayList<LoteRemito> getLotesDelRemito() {
        return lotesDelRemito;
    }

}
