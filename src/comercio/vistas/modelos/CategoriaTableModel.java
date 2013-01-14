package comercio.vistas.modelos;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.CategoriaJpaController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Categoria;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CategoriaTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Nombre", "Descripción"};
    private CategoriaJpaController categoriaJpaController;
    private ArrayList<Categoria> categorias;

    public CategoriaTableModel() {
        super();
        categoriaJpaController = ControllerSingleton.getCategoriaJpaController();
        categoriaJpaController.addObserver(this);
        categorias = categoriaJpaController.obtenerTodasLasCategorias();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return String.class;
            case 1 :
                return String.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName( int c ) {
        return columnsNames[c];
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public int getRowCount() {
        return categorias.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return categorias.get(rowIndex).getNombre();
            case 1 :
                return categorias.get(rowIndex).getDescripcion();
            default :
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Categoria c = categorias.get(rowIndex);
            String valor = String.valueOf(aValue);
            switch (columnIndex){
                case 0 :
                    c.setNombre(valor);
                    categoriaJpaController.edit(c);
                    break;
                case 1 :
                    c.setDescripcion(valor);
                    categoriaJpaController.edit(c);
                    break;
                default:;
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoriaTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CategoriaTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        categorias = categoriaJpaController.obtenerTodasLasCategorias();
        fireTableDataChanged();
    }

    public Categoria obtenerCategoria(Integer index) {
        return categorias.get(index);
    }

}
