package comercio.vistas.modelos;

import comercio.modelo.Lote;
import comercio.modelo.Producto;
import comercio.modelo.Transferencia;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesProximosAVencerTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Producto", "Lote", "Cantidad", "Fecha Producción", "Fecha Vencimiento"};
    private ArrayList<Transferencia> transferencias = new ArrayList();

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
        return transferencias.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return transferencias.get(rowIndex).getLote().getProducto();
            case 1 :
                return transferencias.get(rowIndex).getLote();
            case 2 :
                return transferencias.get(rowIndex).getCantidad();
            case 3 :
                return transferencias.get(rowIndex).getLote().getFechaProduccion();
            case 4 :
                return transferencias.get(rowIndex).getLote().getFechaVencimiento();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param transferencias the transferencias to set
     */
    public void setTransferencias(ArrayList<Transferencia> transferencias) {
        this.transferencias = transferencias;
        fireTableDataChanged();
    }

}
