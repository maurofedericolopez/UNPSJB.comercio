package comercio.vistas.modelos;

import comercio.modelo.Almacen;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class AlmacenComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Almacen> almacenes = new ArrayList();
    private Almacen almacenSeleccionado = null;

    public AlmacenComboBoxModel() {
        super();
    }

    @Override
    public int getSize() {
        return getAlmacenes().size();
    }

    @Override
    public Object getElementAt(int index) {
        return getAlmacenes().get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        setAlmacenSeleccionado((Almacen) anItem);
    }

    @Override
    public Object getSelectedItem() {
        return getAlmacenSeleccionado();
    }

    /**
     * @return the almacenes
     */
    public ArrayList<Almacen> getAlmacenes() {
        return almacenes;
    }

    /**
     * @param almacenes the almacenes to set
     */
    public void setAlmacenes(ArrayList<Almacen> almacenes) {
        this.almacenes = almacenes;
    }

    /**
     * @return the almacenSeleccionado
     */
    public Almacen getAlmacenSeleccionado() {
        return almacenSeleccionado;
    }

    /**
     * @param almacenSeleccionado the almacenSeleccionado to set
     */
    public void setAlmacenSeleccionado(Almacen almacenSeleccionado) {
        this.almacenSeleccionado = almacenSeleccionado;
    }

}
