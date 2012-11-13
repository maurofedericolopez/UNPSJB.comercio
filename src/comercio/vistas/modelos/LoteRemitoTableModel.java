package comercio.vistas.modelos;

import comercio.controladores.LotesController;
import comercio.modelo.Lote;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LoteRemitoTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código","Producto","Cantidad","Fecha Producción","Fecha Vencimiento"};
    private LotesController controlador;
    private ArrayList<Lote> lotes = new ArrayList();;

    public LoteRemitoTableModel() {
        super();
        controlador = new LotesController();
        controlador.addObserver(this);
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Double.class;
            case 3:
                return Long.class;
            case 4:
                return Long.class;
            default:
                return null;
        }
    }

    /**
     * Retorna el nombre de la columna señalada.
     * @param c
     * @return 
     */
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
                return lotes.get(rowIndex).getLote().getCodigo();
            case 1 :
                return lotes.get(rowIndex).getLote().getProducto().getDescripcion();
            case 2 :
                return lotes.get(rowIndex).getCantidad();
            case 3 :
                return lotes.get(rowIndex).getAlmacen().getSucursal();
            case 4 :
                return lotes.get(rowIndex).getAlmacen();
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
        this.fireTableDataChanged();
    }

}
