package comercio.vistas.modelos;

import comercio.controladoresJPA.EgresoJpaController;
import comercio.modelo.LoteEgresado;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class LotesEgresadosTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código", "Producto", "Cantidad", "Fecha Producción", "Fecha Vencimiento"};
    private EgresoJpaController controlador;
    private ArrayList<LoteEgresado> lotesEgresados = new ArrayList();

    public LotesEgresadosTableModel(EgresoJpaController controlador) {
        super();
        this.controlador = controlador;
        this.controlador.addObserver(this);
        lotesEgresados = controlador.getLotesEgresados();
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
        return lotesEgresados.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return lotesEgresados.get(rowIndex).getLote().getCodigo();
            case 1 :
                return lotesEgresados.get(rowIndex).getLote().getProducto().getDescripcion();
            case 2 :
                return lotesEgresados.get(rowIndex).getCantidad();
            case 3 :
                return lotesEgresados.get(rowIndex).getLote().getFechaProduccion();
            case 4 :
                return lotesEgresados.get(rowIndex).getLote().getFechaVencimiento();
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
        lotesEgresados = controlador.getLotesEgresados();
        fireTableDataChanged();
    }

}
