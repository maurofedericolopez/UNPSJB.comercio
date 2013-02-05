package vistas.modelos;

import modelo.Lote;
import modelo.LoteAlmacenado;
import modelo.Producto;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesProximosAVencerTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Producto", "Lote", "Cantidad", "Fecha Producción", "Fecha Vencimiento"};
    private ArrayList<LoteAlmacenado> lotes = new ArrayList();

    public LotesProximosAVencerTableModel() {
        super();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Producto.class;
            case 1:
                return Lote.class;
            case 2:
                return Double.class;
            case 3:
                return Date.class;
            case 4:
                return Date.class;
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
        return lotes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return lotes.get(rowIndex).getLote().getProducto();
            case 1 :
                return lotes.get(rowIndex).getLote();
            case 2 :
                return lotes.get(rowIndex).getCantidad();
            case 3 :
                return lotes.get(rowIndex).getLote().getFechaProduccion();
            case 4 :
                return lotes.get(rowIndex).getLote().getFechaVencimiento();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param lotes the lotes to set
     */
    public void setLotes(ArrayList<LoteAlmacenado> lotes) {
        this.lotes = lotes;
        fireTableDataChanged();
    }

}
