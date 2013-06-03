package vistas.modelos;

import comercio.ControllerSingleton;
import modelo.Origen;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class OrigenComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Origen> origenes;
    private Origen origenSeleccionado = null;

    public OrigenComboBoxModel() {
        super();
        origenes = ControllerSingleton.getOrigenJpaController().obtenerTodosLosOrigenes();
    }

    @Override
    public int getSize() {
        return origenes.size();
    }

    @Override
    public Object getElementAt(int index) {
        return origenes.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        origenSeleccionado = (Origen) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return origenSeleccionado;
    }

}
