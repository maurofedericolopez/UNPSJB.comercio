package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.LoteAlmacenadoJpaController;
import comercio.controladoresJPA.LoteEgresadoJpaController;
import comercio.controladoresJPA.LoteJpaController;
import comercio.controladoresJPA.LoteRemitoJpaController;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import javax.persistence.NoResultException;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesController extends Observable {

    private LoteJpaController controladorLote;
    private LoteAlmacenadoJpaController controladorLoteAlmacenado;
    private LoteEgresadoJpaController controladorLoteEgresado;
    private LoteRemitoJpaController controladorLoteRemito;

    public LotesController() {
        controladorLote = new LoteJpaController(ComercioApp.getEntityManager());
        controladorLoteAlmacenado = new LoteAlmacenadoJpaController(ComercioApp.getEntityManager());
        controladorLoteEgresado = new LoteEgresadoJpaController(ComercioApp.getEntityManager());
        controladorLoteRemito = new LoteRemitoJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<LoteAlmacenado> obtenerLotesAlmacenados() {
        ArrayList<LoteAlmacenado> lotesAlmacenados = new ArrayList();
        Object[] loteAlmacenadoEntities = controladorLoteAlmacenado.findLoteAlmacenadoEntities().toArray();
        for(int i = 0; i < loteAlmacenadoEntities.length; i++)
            lotesAlmacenados.add((LoteAlmacenado) loteAlmacenadoEntities[i]);
        return lotesAlmacenados;
    }

    public Boolean codigoLoteValido(String codigoLote) throws Exception {
        try {
            controladorLote.findLoteByCodigo(codigoLote);
            throw new Exception("El código del lote ya está registrado.");
        } catch (NoResultException ex) {
            return true;
        }
    }

    public Lote crearLote(String codigoLote, Producto producto, Date fechaProduccion, Date fechaVencimiento) {
        Lote lote = new Lote();
        lote.setCodigo(codigoLote);
        lote.setProducto(producto);
        lote.setFechaProduccion(fechaProduccion);
        lote.setFechaVencimiento(fechaVencimiento);
        return lote;
    }

    public LoteRemito crearLoteRemito(Lote lote, Double cantidad) {
        LoteRemito loteRemito = new LoteRemito();
        loteRemito.setLote(lote);
        loteRemito.setCantidadIngresada(cantidad);
        return loteRemito;
    }

    public LoteAlmacenado crearLoteAlmacenado(Lote lote, Double cantidad, Almacen almacen) {
        LoteAlmacenado loteAlmacenado = new LoteAlmacenado();
        loteAlmacenado.setLote(lote);
        loteAlmacenado.setCantidad(cantidad);
        loteAlmacenado.setAlmacen(almacen);
        return loteAlmacenado;
    }

    public void persistirLote(Lote lote) {
        controladorLote.create(lote);
    }

    public void persistirLoteRemito(LoteRemito loteRemito) {
        controladorLoteRemito.create(loteRemito);
    }

    public void persistirLoteAlmacenado(LoteAlmacenado loteAlmacenado) {
        controladorLoteAlmacenado.create(loteAlmacenado);
    }

}
