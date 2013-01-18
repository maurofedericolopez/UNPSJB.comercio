package comercio.vistas.modelos;

import comercio.ControllerSingleton;
import comercio.controladores.ImportacionesController;
import comercio.modelo.LoteRemito;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesDelRemitoTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código", "Producto", "Cantidad", "Fecha Producción", "Fecha Vencimiento"};
    private ImportacionesController controlador;
    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();

    public LotesDelRemitoTableModel() {
        super();
        controlador = ControllerSingleton.getRemitosController();
        lotesDelRemito = controlador.getLotesDelRemito();
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
        return lotesDelRemito.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return lotesDelRemito.get(rowIndex).getLote().getCodigo();
            case 1 :
                return lotesDelRemito.get(rowIndex).getLote().getProducto().getDescripcion();
            case 2 :
                return lotesDelRemito.get(rowIndex).getCantidadIngresada();
            case 3 :
                return lotesDelRemito.get(rowIndex).getLote().getFechaProduccion();
            case 4 :
                return lotesDelRemito.get(rowIndex).getLote().getFechaVencimiento();
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
        lotesDelRemito = controlador.getLotesDelRemito();
        fireTableDataChanged();
    }

}
