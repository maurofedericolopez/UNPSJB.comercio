package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.OrigenJpaController;
import comercio.modelo.Origen;
import java.util.ArrayList;

/**
 *
 * @author Mauro
 */
public class OrigenesController {

    private OrigenJpaController controlador;

    public OrigenesController() {
        super();
        controlador = new OrigenJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Origen> obtenerOrigenes() {
        ArrayList<Origen> origenes = new ArrayList();
        Object[] origenEntities = controlador.findOrigenEntities().toArray();
        for(int i = 0; i < origenEntities.length; i++)
            origenes.add((Origen) origenEntities[i]);
        return origenes;
    }

}
