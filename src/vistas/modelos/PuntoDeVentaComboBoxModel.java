package vistas.modelos;

import comercio.ControllerSingleton;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import modelo.PuntoVenta;

/**
 *
 * @author Mauro Federico Lopez
 */
public class PuntoDeVentaComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<PuntoVenta> puntosDeVenta;
    private PuntoVenta puntoDeVentaSeleccionado = null;

    public PuntoDeVentaComboBoxModel() {
        super();
        puntosDeVenta = ControllerSingleton.getPuntoVentaJpaController().obtenerTodosLosPuntosDeVenta();
    }

    @Override
    public int getSize() {
        return puntosDeVenta.size();
    }

    @Override
    public Object getElementAt(int index) {
        return puntosDeVenta.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        puntoDeVentaSeleccionado = (PuntoVenta) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return puntoDeVentaSeleccionado;
    }

}
