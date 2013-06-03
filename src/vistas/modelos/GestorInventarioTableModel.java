package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.EmpleadoJpaController;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import modelo.GestorInventario;

/**
 *
 * @author Mauro Federico Lopez
 */
public class GestorInventarioTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Apellido","Nombre", "Nombre de usuario", "Contraseña", "Almacén"};
    private EmpleadoJpaController empleadoJpaController;
    private ArrayList<GestorInventario> gestoresDeInventario;

    public GestorInventarioTableModel() {
        super();
        empleadoJpaController = ControllerSingleton.getEmpleadoJpaController();
        empleadoJpaController.addObserver(this);
        gestoresDeInventario = empleadoJpaController.obtenerTodosLosGestoresDeInventario();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return String.class;
            case 1 :
                return String.class;
            case 2 :
                return String.class;
            case 3 :
                return String.class;
            case 4 :
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
        return gestoresDeInventario.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return gestoresDeInventario.get(rowIndex).getApellido();
            case 1 :
                return gestoresDeInventario.get(rowIndex).getNombre();
            case 2 :
                return gestoresDeInventario.get(rowIndex).getUsername();
            case 3 :
                return gestoresDeInventario.get(rowIndex).getPassword();
            case 4 :
                return gestoresDeInventario.get(rowIndex).getAlmacen();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        gestoresDeInventario = empleadoJpaController.obtenerTodosLosGestoresDeInventario();
        fireTableDataChanged();
    }

    public GestorInventario obtenerGestorDeInventario(Integer index) {
        return gestoresDeInventario.get(index);
    }

}
