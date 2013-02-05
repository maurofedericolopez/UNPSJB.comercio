package vistas.modelos;

import modelo.Egreso;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class EgresoTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Código", "Causa Especial", "Fecha", "Observaciones", "Sucursal", "Almacén"};
    private ArrayList<Egreso> egresos = new ArrayList();

    public EgresoTableModel() {
        super();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return String.class;
            case 1 :
                return String.class;
            case 2 :
                return Date.class;
            case 3 :
                return String.class;
            case 4 :
                return Long.class;
            case 5 :
                return Long.class;
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
        return egresos.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return egresos.get(rowIndex).getCodigo();
            case 1 :
                return egresos.get(rowIndex).getCausaEspecial();
            case 2 :
                return egresos.get(rowIndex).getFecha();
            case 3 :
                return egresos.get(rowIndex).getObservaciones();
            case 4 :
                return egresos.get(rowIndex).getAlmacen().getSucursal().getNumero();
            case 5 :
                return egresos.get(rowIndex).getAlmacen().getNumero();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param egresos the egresos to set
     */
    public void setEgresos(ArrayList<Egreso> egresos) {
        this.egresos = egresos;
        fireTableDataChanged();
    }

    public Egreso obtenerEgreso(int index) {
        return egresos.get(index);
    }

}
