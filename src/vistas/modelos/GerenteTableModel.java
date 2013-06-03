package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.EmpleadoJpaController;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import modelo.Gerente;

/**
 *
 * @author Mauro Federico Lopez
 */
public class GerenteTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Apellido","Nombre", "Nombre de usuario", "Contraseña", "Sucursal"};
    private EmpleadoJpaController empleadoJpaController;
    private ArrayList<Gerente> gerentes;

    public GerenteTableModel() {
        super();
        empleadoJpaController = ControllerSingleton.getEmpleadoJpaController();
        empleadoJpaController.addObserver(this);
        gerentes = empleadoJpaController.obtenerTodosLosGerentes();
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
        return gerentes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return gerentes.get(rowIndex).getApellido();
            case 1 :
                return gerentes.get(rowIndex).getNombre();
            case 2 :
                return gerentes.get(rowIndex).getUsername();
            case 3 :
                return gerentes.get(rowIndex).getPassword();
            case 4 :
                return gerentes.get(rowIndex).getSucursal();
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
        gerentes = empleadoJpaController.obtenerTodosLosGerentes();
        fireTableDataChanged();
    }

    public Gerente obtenerGerente(Integer index) {
        return gerentes.get(index);
    }

}
