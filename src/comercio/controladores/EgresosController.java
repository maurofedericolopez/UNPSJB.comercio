package comercio.controladores;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.AlmacenJpaController;
import comercio.controladoresJPA.EgresoJpaController;
import comercio.controladoresJPA.LoteEgresadoJpaController;
import comercio.controladoresJPA.LoteJpaController;
import comercio.modelo.Lote;
import comercio.modelo.LoteEgresado;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class EgresosController extends Observable {

    private EgresoJpaController egresoJpaController;
    private AlmacenJpaController almacenJpaController;
    private LoteJpaController loteJpaController;
    private LoteEgresadoJpaController loteEgresadoJpaController;
    private ArrayList<LoteEgresado> lotesEgresados = new ArrayList();

    public EgresosController() {
        egresoJpaController = ControllerSingleton.getEgresoJpaController();
        almacenJpaController = ControllerSingleton.getAlmacenJpaController();
        loteJpaController = ControllerSingleton.getLoteJpaController();
        loteEgresadoJpaController = ControllerSingleton.getLoteEgresadoJpaController();
    }

    public void agregarLote(String codigoLote, Double cantidad) throws Exception {
        Lote lote = loteJpaController.BuscarLotePorCodigo(codigoLote);
    }

    /**
     * @return the lotesEgresados
     */
    public ArrayList<LoteEgresado> getLotesEgresados() {
        return lotesEgresados;
    }

}
