package comercio.vistas.modelos;

import comercio.controladores.MarcasController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Marca;
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
    private MarcasController marcaController;
    private ArrayList<Marca> marcas;

    public MarcaTableModel(MarcasController marcaController) {
        super();
        this.marcaController = marcaController;
        this.marcaController.addObserver(this);
        marcas = marcaController.obtenerMarcas();
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
            switch (columnIndex){
                case 0 :
                    m.setNombre(String.valueOf(aValue));
                    marcaController.editarNombreMarca(m, aValue);
                    break;
                case 1 :
                    m.setAbreviacion(String.valueOf(aValue));
                    marcaController.editarAbreviacionMarca(m, aValue);
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
        marcas = marcaController.obtenerMarcas();
        this.fireTableDataChanged();
    }

    public Marca obtenerMarca(int selectedRow) {
        return marcas.get(selectedRow);
    }

}
