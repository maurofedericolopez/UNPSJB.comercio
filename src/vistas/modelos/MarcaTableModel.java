package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.MarcaJpaController;
import controladoresJPA.exceptions.NonexistentEntityException;
import modelo.Marca;
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
public class MarcaTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Nombre", "Abreviación"};
    private MarcaJpaController marcaController;
    private ArrayList<Marca> marcas;

    public MarcaTableModel() {
        super();
        marcaController = ControllerSingleton.getMarcaJpaController();
        marcaController.addObserver(this);
        marcas = marcaController.obtenerTodasLasMarcas();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
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
        return marcas.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return marcas.get(rowIndex).getNombre();
            case 1 :
                return marcas.get(rowIndex).getAbreviacion();
            default :
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Marca m = marcas.get(rowIndex);
            String valor = String.valueOf(aValue);
            switch (columnIndex){
                case 0 :
                    m.setNombre(valor);
                    marcaController.editarMarca(m);
                    break;
                case 1 :
                    m.setAbreviacion(valor);
                    marcaController.editarMarca(m);
                    break;
                default:;
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MarcaTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MarcaTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        marcas = marcaController.obtenerTodasLasMarcas();
        fireTableDataChanged();
    }

    public Marca obtenerMarca(int index) {
        return marcas.get(index);
    }

}
