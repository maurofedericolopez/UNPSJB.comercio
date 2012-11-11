package comercio.vistas.modelos;

import comercio.controladores.CategoriasController;
import comercio.modelo.Categoria;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CategoriaComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private ArrayList<Categoria> categorias;
    private Categoria categoriaSeleccionada;

    public CategoriaComboBoxModel() {
        categorias = new CategoriasController().obtenerCategorias();
    }

    @Override
    public int getSize() {
        return categorias.size();
    }

    @Override
    public Object getElementAt(int index) {
        return categorias.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        categoriaSeleccionada = (Categoria) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return categoriaSeleccionada;
    }

}
