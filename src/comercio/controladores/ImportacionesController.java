package comercio.controladores;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.*;
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
    private LoteAlmacenadoJpaController loteAlmacenadoJpaController;
    private LoteRemitoJpaController loteRemitoJpaController;
    private LoteJpaController loteJpaController;
    private ProductoJpaController productoJpaController;

    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();
    private Remito remito = null;
    private Almacen almacen = null;

    public ImportacionesController() {
        super();
        loteAlmacenadoJpaController = ControllerSingleton.getLoteAlmacenadoJpaController();
        loteRemitoJpaController = ControllerSingleton.getLoteRemitoJpaController();
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
            lotesDelRemito.add(loteRemito);

            notificarCambios();
        }
    }

    public void eliminarLote(LoteRemito loteRemito) {
        lotesDelRemito.remove(loteRemito);
    }

    public void registrarDatosRemito(String codigoRemito, Date fechaRemito, Object almacen) throws Exception {
        if (almacen != null) {
            if (fechaRemito != null) {
                setNuevoRemito(new Remito());
                getNuevoRemito().setCodigo(codigoRemito.toUpperCase());
                getNuevoRemito().setFecha((Date) fechaRemito);
                setAlmacen((Almacen) almacen);
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
        Iterator<LoteRemito> i = lotesDelRemito.iterator();
        while(i.hasNext()) {
            LoteRemito loteRemito = i.next();

            Lote lote = loteRemito.getLote();
            loteJpaController.create(lote);

            loteRemito.setRemito(remito);
            loteRemitoJpaController.create(loteRemito);

            Double cantidad = loteRemito.getCantidadIngresada();
            LoteAlmacenado loteAlmacenado = new LoteAlmacenado();
            loteAlmacenado.setAlmacen(almacen);
            loteAlmacenado.setLote(lote);
            loteAlmacenado.setCantidad(cantidad);
            loteAlmacenadoJpaController.create(loteAlmacenado);
        }
    }

    public void cancelarIngresoDeLotesDeProductos() {
        limpiarVariables();
    }

    private Boolean codigoNoSeAgrego(String codigo) {
        Iterator<LoteRemito> i = lotesDelRemito.iterator();
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

    /**
     * @param lotesDelRemito the lotesDelRemito to set
     */
    public void setLotesDelRemito(ArrayList<LoteRemito> lotesDelRemito) {
        this.lotesDelRemito = lotesDelRemito;
    }

    /**
     * @return the remito
     */
    public Remito getNuevoRemito() {
        return remito;
    }

    /**
     * @param remito the remito to set
     */
    public void setNuevoRemito(Remito nuevoRemito) {
        this.remito = nuevoRemito;
    }

    /**
     * @return the almacen
     */
    public Almacen getAlmacen() {
        return almacen;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

}
