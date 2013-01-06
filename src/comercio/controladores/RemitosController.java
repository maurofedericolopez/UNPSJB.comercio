package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.*;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Mauro Federico Lopez
 */
public class RemitosController extends Observable {

    private LotesController lotesController;
    private RemitoJpaController controladorRemito;
    private LoteAlmacenadoJpaController controladorLoteAlmacenado;
    private LoteRemitoJpaController controladorLoteRemito;
    private LoteJpaController controladorLote;
    private ProductoJpaController controladorProducto;
    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();
    private Remito nuevoRemito;
    private Almacen almacen;

    public RemitosController() {
        super();
        EntityManagerFactory emf = ComercioApp.getEntityManager();
        lotesController = new LotesController();
        controladorProducto = new ProductoJpaController(emf);
        controladorLoteAlmacenado = new LoteAlmacenadoJpaController(emf);
        controladorLoteRemito = new LoteRemitoJpaController(emf);
        controladorRemito = new RemitoJpaController(emf);
        controladorLote = new LoteJpaController(emf);
    }

    public ArrayList<LoteRemito> obtenerLotesDelRemito() {
        return getLotesDelRemito();
    }

    public void registrarLoteRemito(LoteRemito loteRemito) {
        getLotesDelRemito().add(loteRemito);
        setChanged();
        notifyObservers();
    }

    public void registrarDatosRemito(String codigoRemito, Object fechaRemito, Object almacen) {
        setNuevoRemito(new Remito());
        getNuevoRemito().setCodigo(codigoRemito.toUpperCase());
        getNuevoRemito().setFecha((Date) fechaRemito);
        setAlmacen((Almacen) almacen);
        setChanged();
        notifyObservers();
    }

    public void persistirOperacion() {
        controladorRemito.create(nuevoRemito);
        Iterator<LoteRemito> i = lotesDelRemito.iterator();
        while(i.hasNext()) {
            LoteRemito loteRemito = i.next();
            lotesController.persistirLote(loteRemito.getLote());
            loteRemito.setRemito(nuevoRemito);
            lotesController.persistirLoteRemito(loteRemito);
            LoteAlmacenado loteAlmacenado = lotesController.crearLoteAlmacenado(loteRemito.getLote(), loteRemito.getCantidadIngresada(), almacen);
            lotesController.persistirLoteAlmacenado(loteAlmacenado);
        }
    }

    /**
     * @return the controladorRemito
     */
    public RemitoJpaController getControladorRemito() {
        return controladorRemito;
    }

    /**
     * @param controladorRemito the controladorRemito to set
     */
    public void setControladorRemito(RemitoJpaController controladorRemito) {
        this.controladorRemito = controladorRemito;
    }

    /**
     * @return the controladorLoteRemito
     */
    public LoteRemitoJpaController getControladorLoteRemito() {
        return controladorLoteRemito;
    }

    /**
     * @param controladorLoteRemito the controladorLoteRemito to set
     */
    public void setControladorLoteRemito(LoteRemitoJpaController controladorLoteRemito) {
        this.controladorLoteRemito = controladorLoteRemito;
    }

    /**
     * @return the controladorLote
     */
    public LoteJpaController getControladorLote() {
        return controladorLote;
    }

    /**
     * @param controladorLote the controladorLote to set
     */
    public void setControladorLote(LoteJpaController controladorLote) {
        this.controladorLote = controladorLote;
    }

    /**
     * @return the controladorProducto
     */
    public ProductoJpaController getControladorProducto() {
        return controladorProducto;
    }

    /**
     * @param controladorProducto the controladorProducto to set
     */
    public void setControladorProducto(ProductoJpaController controladorProducto) {
        this.controladorProducto = controladorProducto;
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
     * @return the nuevoRemito
     */
    public Remito getNuevoRemito() {
        return nuevoRemito;
    }

    /**
     * @param nuevoRemito the nuevoRemito to set
     */
    public void setNuevoRemito(Remito nuevoRemito) {
        this.nuevoRemito = nuevoRemito;
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

    /**
     * @return the controladorLoteAlmacenado
     */
    public LoteAlmacenadoJpaController getControladorLoteAlmacenado() {
        return controladorLoteAlmacenado;
    }

    /**
     * @param controladorLoteAlmacenado the controladorLoteAlmacenado to set
     */
    public void setControladorLoteAlmacenado(LoteAlmacenadoJpaController controladorLoteAlmacenado) {
        this.controladorLoteAlmacenado = controladorLoteAlmacenado;
    }

}
