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
public class LoteRemitoTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código", "Producto", "Cantidad", "Fecha Producción", "Fecha Vencimiento"};
    private ImportacionesController controlador;
    private ArrayList<LoteRemito> lotesDelRemito = new ArrayList();

    public LoteRemitoTableModel() {
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
        return getColumnsNames()[c];
    }

    @Override
    public int getColumnCount() {
        return getColumnsNames().length;
    }

    @Override
    public int getRowCount() {
        return getLotesDelRemito().size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return getLotesDelRemito().get(rowIndex).getLote().getCodigo();
            case 1 :
                return getLotesDelRemito().get(rowIndex).getLote().getProducto().getDescripcion();
            case 2 :
                return getLotesDelRemito().get(rowIndex).getCantidadIngresada();
            case 3 :
                return getLotesDelRemito().get(rowIndex).getLote().getFechaProduccion();
            case 4 :
                return getLotesDelRemito().get(rowIndex).getLote().getFechaVencimiento();
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

    /**
     * @return the columnsNames
     */
    public String[] getColumnsNames() {
        return columnsNames;
    }

    /**
     * @param columnsNames the columnsNames to set
     */
    public void setColumnsNames(String[] columnsNames) {
        this.columnsNames = columnsNames;
    }

    /**
     * @return the controlador
     */
    public ImportacionesController getControlador() {
        return controlador;
    }

    /**
     * @param controlador the controlador to set
     */
    public void setControlador(ImportacionesController controlador) {
        this.controlador = controlador;
    }

    /**
     * @return the lotesDelRemito
     */
    public ArrayList<LoteRemito> getLotesDelRemito() {
        return lotesDelRemito;
    }

    /**
     * @param lotesDelRemito the lotesDelRemito to set
     */
    public void setLotesDelRemito(ArrayList<LoteRemito> lotesDelRemito) {
        this.lotesDelRemito = lotesDelRemito;
    }

}
