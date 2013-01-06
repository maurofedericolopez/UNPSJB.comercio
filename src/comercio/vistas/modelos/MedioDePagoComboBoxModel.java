package comercio.vistas.modelos;

import comercio.ControllerSingleton;
import comercio.controladores.CategoriasController;
import comercio.controladores.VentasController;
import comercio.modelo.Categoria;
import comercio.modelo.MedioDePago;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class MedioDePagoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<MedioDePago> mediosDePago;
    private MedioDePago medioDePagoSeleccionado;

    public MedioDePagoComboBoxModel() {
        super();
        mediosDePago = ControllerSingleton.getVentasController().obtenerMediosDePago();
    }

    @Override
    public int getSize() {
        return mediosDePago.size();
    }

    @Override
    public Object getElementAt(int index) {
        return mediosDePago.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        medioDePagoSeleccionado = (MedioDePago) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return medioDePagoSeleccionado;
    }

}
