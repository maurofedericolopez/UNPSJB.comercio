package comercio.vistas.modelos;

import comercio.ControllerSingleton;
import comercio.modelo.Unidad;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class UnidadComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Unidad> unidades;
    private Unidad unidadSeleccionada = null;

    public UnidadComboBoxModel() {
        super();
        unidades = ControllerSingleton.getUnidadJpaController().obtenerTodasLasUnidades();
    }

    @Override
    public int getSize() {
        return unidades.size();
    }

    @Override
    public Object getElementAt(int index) {
        return unidades.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        unidadSeleccionada = (Unidad) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return unidadSeleccionada;
    }

}
