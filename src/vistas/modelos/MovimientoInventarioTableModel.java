package vistas.modelos;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import modelo.Operacion;

/**
 *
 * @author Mauro Federico Lopez
 */
public class MovimientoInventarioTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Empleado", "Fecha", "Descripción"};
    private ArrayList<Operacion> operaciones;

    public MovimientoInventarioTableModel() {
        super();
        operaciones = new ArrayList();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return String.class;
            case 1 :
                return Date.class;
            case 2 :
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
        return operaciones.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return operaciones.get(rowIndex).getEmpleado().toString();
            case 1 :
                return operaciones.get(rowIndex).getFecha();
            case 2 :
                return operaciones.get(rowIndex).getDescripcion();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param operaciones the operaciones to set
     */
    public void setOperaciones(ArrayList<Operacion> operaciones) {
        this.operaciones = operaciones;
        fireTableDataChanged();
    }

}
