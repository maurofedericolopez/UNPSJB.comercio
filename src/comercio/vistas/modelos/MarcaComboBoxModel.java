package comercio.vistas.modelos;

import comercio.ControllerSingleton;
import comercio.modelo.Marca;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class MarcaComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Marca> marcas;
    private Marca marcaSeleccionada = null;

    public MarcaComboBoxModel() {
        super();
        marcas = ControllerSingleton.getMarcaJpaController().obtenerTodasLasMarcas();
    }

    @Override
    public int getSize() {
        return marcas.size();
    }

    @Override
    public Object getElementAt(int index) {
        return marcas.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        marcaSeleccionada = (Marca) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return marcaSeleccionada;
    }

}
