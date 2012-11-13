package comercio.controladores;

import comercio.controladoresJPA.LoteAlmacenadoJpaController;
import comercio.controladoresJPA.LoteEgresadoJpaController;
import comercio.controladoresJPA.LoteJpaController;
import comercio.controladoresJPA.LoteRemitoJpaController;
import comercio.modelo.LoteAlmacenado;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesController extends Observable {

    private LoteJpaController controladorLote;
    private LoteAlmacenadoJpaController controladorLoteAlmacenado;
    private LoteEgresadoJpaController controladorLoteEgresado;
    private LoteRemitoJpaController controladorLoteRemito;

    public LotesController() {}

    public ArrayList<LoteAlmacenado> obtenerLotesAlmacenados() {
        ArrayList<LoteAlmacenado> lotesAlmacenados = new ArrayList();
        Object[] loteAlmacenadoEntities = controladorLoteAlmacenado.findLoteAlmacenadoEntities().toArray();
        for(int i = 0; i < loteAlmacenadoEntities.length; i++)
            lotesAlmacenados.add((LoteAlmacenado) loteAlmacenadoEntities[i]);
        return lotesAlmacenados;
    }

}
