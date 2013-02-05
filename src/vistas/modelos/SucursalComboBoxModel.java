package vistas.modelos;

import comercio.ControllerSingleton;
import modelo.Sucursal;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class SucursalComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Sucursal> sucursales;
    private Sucursal sucursalSeleccionada = null;

    public SucursalComboBoxModel() {
        super();
        sucursales = ControllerSingleton.getSucursalJpaController().obtenerTodasLasSucursales();
    }

    @Override
    public int getSize() {
        return sucursales.size();
    }

    @Override
    public Object getElementAt(int index) {
        return sucursales.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        sucursalSeleccionada = (Sucursal) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return sucursalSeleccionada;
    }

}
