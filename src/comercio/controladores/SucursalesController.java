package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.AlmacenJpaController;
import comercio.controladoresJPA.PuntoVentaJpaController;
import comercio.controladoresJPA.SucursalJpaController;
import comercio.modelo.Almacen;
import comercio.modelo.LoteAlmacenado;
import comercio.modelo.PuntoVenta;
import comercio.modelo.Sucursal;
import java.util.ArrayList;

/**
 *
 * @author Mauro Federico Lopez
 */
public class SucursalesController {

    private SucursalJpaController sucursalJpaController;
    private AlmacenJpaController almacenJpaController;
    private PuntoVentaJpaController puntoVentaJpaController;

    public SucursalesController() {
        sucursalJpaController = new SucursalJpaController(ComercioApp.getEntityManager());
        almacenJpaController = new AlmacenJpaController(ComercioApp.getEntityManager());
        puntoVentaJpaController = new PuntoVentaJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Sucursal> obtenerSucursales() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<Almacen> obtenerAlmacenesDeSucursal(Object sucursalSeleccionada) {
        ArrayList<Almacen> almacenes = new ArrayList();
        if(sucursalSeleccionada != null) {
            Sucursal s = (Sucursal) sucursalSeleccionada;
            Object[] almacenEntities = almacenJpaController.buscarAlmacenesPorSucursal(s.getId()).toArray();
            for(int i = 0; i < almacenEntities.length; i++) {
                almacenes.add((Almacen) almacenEntities[i]);
            }
        }
        return almacenes;
    }

    public ArrayList<Almacen> obtenerAlmacenes() {
        ArrayList<Almacen> almacenes = new ArrayList();
        Object[] almacenEntities = almacenJpaController.findAlmacenEntities().toArray();
        for(int i = 0; i < almacenEntities.length; i++)
            almacenes.add((Almacen) almacenEntities[i]);
        return almacenes;
    }

    public ArrayList<PuntoVenta> obtenerPuntosDeVenta() {
        ArrayList<PuntoVenta> puntosDeVenta = new ArrayList();
        Object[] array = puntoVentaJpaController.findPuntoVentaEntities().toArray();
        for(int i = 0; i < array.length; i++)
            puntosDeVenta.add((PuntoVenta) array[i]);
        return puntosDeVenta;
    }

    public void descontarDeAlmacen(Almacen almacen, String codigoLote, Double cantidad) throws Exception {
        Object[] array = almacen.getLotesAlmacenados().toArray();
        for(Object o : array) {
            LoteAlmacenado la = (LoteAlmacenado) o;
            if(la.getLote().getCodigo().equals(codigoLote)) {
                if(la.getCantidad() >= cantidad) {
                    la.setCantidad(la.getCantidad() - cantidad);
                    break;
                } else {
                    throw new Exception("No hay cantidad suficiente para satisfacer la transferencia.");
                }
            }
        }
        throw new Exception("El almacén no contienen ningun lote con el código de lote ingresado.");
    }

}
