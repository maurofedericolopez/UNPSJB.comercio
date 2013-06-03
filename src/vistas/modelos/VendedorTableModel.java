package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.EmpleadoJpaController;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import modelo.Vendedor;

/**
 *
 * @author Mauro Federico Lopez
 */
public class VendedorTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Apellido","Nombre", "Nombre de usuario", "Contraseña", "Punto de venta"};
    private EmpleadoJpaController empleadoJpaController;
    private ArrayList<Vendedor> vendedores;

    public VendedorTableModel() {
        super();
        empleadoJpaController = ControllerSingleton.getEmpleadoJpaController();
        empleadoJpaController.addObserver(this);
        vendedores = empleadoJpaController.obtenerTodosLosVendedores();
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
        return vendedores.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return vendedores.get(rowIndex).getApellido();
            case 1 :
                return vendedores.get(rowIndex).getNombre();
            case 2 :
                return vendedores.get(rowIndex).getUsername();
            case 3 :
                return vendedores.get(rowIndex).getPassword();
            case 4 :
                return vendedores.get(rowIndex).getPuntoVenta();
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
        vendedores = empleadoJpaController.obtenerTodosLosVendedores();
        fireTableDataChanged();
    }

    public Vendedor obtenerVendedor(Integer index) {
        return vendedores.get(index);
    }

}
