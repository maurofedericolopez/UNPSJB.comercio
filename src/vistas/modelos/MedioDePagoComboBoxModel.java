package vistas.modelos;

import comercio.ControllerSingleton;
import modelo.MedioDePago;
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
        mediosDePago = ControllerSingleton.getMedioDePagoJpaController().obtenerTodosLosMediosDePago();
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
