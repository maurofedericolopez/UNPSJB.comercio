package comercio.vistas.modelos;

import comercio.controladores.CategoriasController;
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
    private CategoriasController categoriaController;
    private ArrayList<Categoria> categorias;

    public CategoriaTableModel(CategoriasController categoriaController) {
        super();
        this.categoriaController = categoriaController;
        this.categoriaController.addObserver(this);
        categorias = categoriaController.obtenerCategorias();
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
            switch (columnIndex){
                case 0 :
                    categoriaController.editarNombreCategoria(c, aValue);
                    break;
                case 1 :
                    categoriaController.editarDescripcionCategoria(c, aValue);
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
        categorias = categoriaController.obtenerCategorias();
        this.fireTableDataChanged();
    }

    public Categoria obtenerCategoria(int selectedRow) {
        return categorias.get(selectedRow);
    }

}
