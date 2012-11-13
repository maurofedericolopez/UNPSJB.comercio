package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.UnidadJpaController;
import comercio.modelo.Unidad;
import java.util.ArrayList;

/**
 *
 * @author Mauro
 */
public class UnidadesController {

    private UnidadJpaController controlador;

    public UnidadesController() {
        super();
        controlador = new UnidadJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Unidad> obtenerUnidades() {
        ArrayList<Unidad> unidades = new ArrayList();
        Object[] unidadEntities = controlador.findUnidadEntities().toArray();
        for(int i = 0; i < unidadEntities.length; i++)
            unidades.add((Unidad) unidadEntities[i]);
        return unidades;
    }
}
