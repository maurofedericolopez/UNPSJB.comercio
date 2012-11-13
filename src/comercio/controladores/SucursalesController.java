package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.AlmacenJpaController;
import comercio.controladoresJPA.SucursalJpaController;
import comercio.modelo.Almacen;
import comercio.modelo.Sucursal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mauro Federico Lopez
 */
public class SucursalesController {

    private SucursalJpaController controladorSucursal;
    private AlmacenJpaController controladorAlmacen;

    public SucursalesController() {
        controladorSucursal = new SucursalJpaController(ComercioApp.getEntityManager());
        controladorAlmacen = new AlmacenJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Sucursal> obtenerSucursales() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<Almacen> obtenerAlmacenesDeSucursal(Object sucursalSeleccionada) {
        ArrayList<Almacen> almacenes = new ArrayList();
        if(sucursalSeleccionada != null) {
            Sucursal s = (Sucursal) sucursalSeleccionada;
            Object[] almacenEntities = controladorAlmacen.buscarAlmacenesPorSucursal(s.getId()).toArray();
            for(int i = 0; i < almacenEntities.length; i++) {
                almacenes.add((Almacen) almacenEntities[i]);
            }
        }
        return almacenes;
    }

}
