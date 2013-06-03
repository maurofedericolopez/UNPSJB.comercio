package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.ProductoJpaController;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import modelo.Producto;

/**
 *
 * @author Mauro Federico Lopez
 */
public class VarianzaTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Producto", "Varianza"};
    private ProductoJpaController productoJpaController;
    private ArrayList<Producto> productos;
    private Date desde;
    private Date hasta;

    public VarianzaTableModel() {
        super();
        productoJpaController = ControllerSingleton.getProductoJpaController();
        productos = new ArrayList();
        desde = new Date();
        hasta = new Date();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
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
        return productos.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return productos.get(rowIndex).getCodigo();
            case 1 :
                return productoJpaController.varianzaDelPrecioDeUnProducto(desde, hasta, productos.get(rowIndex));
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void actualizarTabla(Date desde, Date hasta, ArrayList<Producto> productos) {
        this.desde = desde;
        this.hasta = hasta;
        this.productos = productos;
        this.fireTableDataChanged();
    }

}
