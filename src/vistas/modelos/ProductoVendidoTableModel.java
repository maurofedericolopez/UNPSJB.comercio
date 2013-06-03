package vistas.modelos;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import modelo.ItemVenta;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductoVendidoTableModel extends AbstractTableModel {

    private String[] columnsNames = {"Producto", "Cantidad Vendida", "Total"};
    private ArrayList<ItemVenta> productosVendidos;

    public ProductoVendidoTableModel() {
        super();
        productosVendidos = new ArrayList();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return String.class;
            case 1 :
                return Double.class;
            case 2 :
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
        return productosVendidos.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0 :
                return productosVendidos.get(rowIndex).getProducto().getDescripcion();
            case 1 :
                return productosVendidos.get(rowIndex).getCantidad();
            case 2 :
                return productosVendidos.get(rowIndex).getTotal();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * @param productosVendidos the productosVendidos to set
     */
    public void setProductosVendidos(ArrayList<ItemVenta> productosVendidos) {
        this.productosVendidos = productosVendidos;
        fireTableDataChanged();
    }

}
