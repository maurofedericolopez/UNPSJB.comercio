package comercio.vistas.modelos;

import comercio.controladores.UnidadesController;
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
        unidades = new UnidadesController().obtenerUnidades();
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
