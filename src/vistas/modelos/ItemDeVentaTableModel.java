package vistas.modelos;

import controladoresJPA.VentaJpaController;
import modelo.ItemVenta;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ItemDeVentaTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Producto", "Precio", "Cantidad", "Descuento"};
    private VentaJpaController ventaJpaController;
    private ArrayList<ItemVenta> itemsDeVenta;

    public ItemDeVentaTableModel(VentaJpaController ventaJpaController) {
        super();
        this.ventaJpaController = ventaJpaController;
        this.ventaJpaController.addObserver(this);
        itemsDeVenta = this.ventaJpaController.getItemsDeVenta();
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
            case 3 :
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
        return itemsDeVenta.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0 :
                return itemsDeVenta.get(rowIndex).getProducto().getDescripcion();
            case 1 :
                return itemsDeVenta.get(rowIndex).getProducto().getPrecioActual();
            case 2 :
                return itemsDeVenta.get(rowIndex).getCantidad();
            case 3 :
                return 1 - itemsDeVenta.get(rowIndex).getDescuento();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        itemsDeVenta = ventaJpaController.getItemsDeVenta();
        fireTableDataChanged();
    }

}
